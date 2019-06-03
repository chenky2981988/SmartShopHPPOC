package com.hppoc.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.UUID;

import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;
    private static final int BOT_IMAGE = 10003;
    private static final int BOT_NAVIGATE = 10004;

    private String uuid = UUID.randomUUID().toString();
    private LinearLayout chatLayout;
    private EditText queryEditText;
    private ImageView mapButton;

    // Android client
    private AIRequest aiRequest;
    private AIDataService aiDataService;
    private AIServiceContext customAIServiceContext;

    // Java V2
    private SessionsClient sessionsClient;
    private SessionName session;
    private String itemKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ScrollView scrollview = findViewById(R.id.chatScrollView);
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);
        mapButton = findViewById(R.id.mapBtn);

        ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this::sendMessage);

        queryEditText = findViewById(R.id.queryEditText);
        queryEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        sendMessage(sendBtn);
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("EVENT","SHOW_MAP");
                startActivity(intent);
            }
        });


        // Android client
        initChatbot();

        // Java V2
        //  initV2Chatbot();
    }

    private void initChatbot() {
        final AIConfiguration config = new AIConfiguration(BuildConfig.ClientAccessToken,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(this, config);
        customAIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);// helps to create new session whenever app restarts
        aiRequest = new AIRequest();
    }

    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.test_agent_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);
            Log.d("TAG", "Random UUID : " + uuid);
            Log.d("TAG", "Project : " + session.getProject());
            Log.d("TAG", "Session : " + session.getSession());
            Log.d("TAG", "Session Str : " + session.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(View view) {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter your query!", Toast.LENGTH_LONG).show();
        } else {
            showTextView(msg, null, USER);
            queryEditText.setText("");
//            // Android client
            aiRequest.setQuery(msg);
            RequestTask requestTask = new RequestTask(MainActivity.this, aiDataService, customAIServiceContext);
            requestTask.execute(aiRequest);

            // Java V2
//            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("en-US")).build();
//            new RequestJavaV2Task(MainActivity.this, session, sessionsClient, queryInput).execute();
        }
    }

    public void callback(AIResponse aiResponse) {
        if (aiResponse != null) {
            // process aiResponse here
            // Log.d(TAG,"Response MessageSize : " + aiResponse.getResult().getFulfillment());
            if (aiResponse.getStatus().getCode() == 200) {
                Log.d(TAG, "Intent Name : " + aiResponse.getResult().getMetadata().getIntentName());
                if(aiResponse.getResult().getMetadata().getIntentName().equalsIgnoreCase("FindThings") && aiResponse.getResult().getFulfillment().getMessages().size() > 1)
                {
                    if(aiResponse.getResult().getParameters().containsKey("item")) {
                        itemKey = aiResponse.getResult().getParameters().get("item").getAsString();
                        Log.d(TAG,"itemKey : " + itemKey);
                    }
                    Log.d(TAG, "Messages Size : " + aiResponse.getResult().getFulfillment().getMessages().size());
                    ResponseMessage responseMessage = aiResponse.getResult().getFulfillment().getMessages().get(1);
                    String responseStr = new Gson().toJson(responseMessage).toString();
                    Log.d(TAG, "Json String : " + responseStr);

                    ResponseMessageObj responseMessageObj = new Gson().fromJson(responseStr, ResponseMessageObj.class);
                    Log.d(TAG, "Bot Reply: " + responseMessageObj.getSubtitle());
                    Log.d(TAG, "Img URL : " + responseMessageObj.getImageUrl());
                    showTextView(responseMessageObj.getSubtitle(), responseMessageObj.getImageUrl(), BOT_IMAGE);
                } else if(aiResponse.getResult().getMetadata().getIntentName().equalsIgnoreCase("navigate")){
                    String botReply = aiResponse.getResult().getFulfillment().getSpeech();
                    Log.d(TAG, "Resposne Message 2nd Obj : " + botReply);
                    showTextView(botReply, null, BOT_NAVIGATE);
                }else {
                    String botReply = aiResponse.getResult().getFulfillment().getSpeech();
                    Log.d(TAG, "Resposne Message 2nd Obj : " + botReply);
                    showTextView(botReply, null, BOT);
                }

            }
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Please Try again!", null, BOT);
        }
    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            // process aiResponse here
            String botReply = response.getQueryResult().getFulfillmentText();
            Log.d(TAG, "V2 Bot Reply: " + botReply);
            showTextView(botReply, null, BOT);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextView("There was some communication issue. Please Try again!", null, BOT);
        }
    }

    private void showTextView(String message, String imgUrl, int type) {
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            case BOT_IMAGE:
                layout = getBotImageLayout();
                break;
            case BOT_NAVIGATE:
                layout = getBotNavigateLayout();
                MapView mapView = layout.findViewById(R.id.mapview);
                mapView.setAppKey(EditorKey.forApp(BuildConfig.ArubaAppKey));
                mapView.setMapKey(EditorKey.forMap(BuildConfig.ArubaMapKey, BuildConfig.ArubaAppKey));
                MapOptions mapOptions = mapView.getOptions();
                mapOptions.HIDE_MAP_LABEL = true;
                mapOptions.HIDE_DIRECTIONS_CONTROLS = true;
                mapOptions.HIDE_OVERVIEW_BUTTON = true;
                mapOptions.HIDE_ACCESSIBILITY_BUTTON = true;
                mapOptions.HIDE_LEVELS_CONTROL = true;
                mapOptions.HIDE_LOCATION_BUTTON = true;
                mapView.setOptions(mapOptions);

                Button navigateButton = layout.findViewById(R.id.navigateButton);
                navigateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
                        intent.putExtra("EVENT","NAVIGATE");
                        intent.putExtra("ITEM_KEY",itemKey);
                        startActivity(intent);
                    }
                });
                break;
            default:
                layout = getBotLayout();
                break;
        }
        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
        TextView tv = layout.findViewById(R.id.chatMsg);
        tv.setText(message);
        if (!TextUtils.isEmpty(imgUrl)) {
            ImageView imageView = layout.findViewById(R.id.imageView);
            Picasso.get()
                    .load(imgUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    //.error(R.drawable.user_placeholder_error)
                    .into(imageView);
        }
        layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }

    FrameLayout getBotImageLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_image_layout, null);
    }

    FrameLayout getBotNavigateLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_navigate, null);
    }
}

