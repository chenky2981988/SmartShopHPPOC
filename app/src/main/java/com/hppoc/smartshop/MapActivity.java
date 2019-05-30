package com.hppoc.smartshop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.arubanetworks.meridian.campaigns.CampaignsService;
import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.internal.util.Strings;
import com.arubanetworks.meridian.location.LocationRequest;
import com.arubanetworks.meridian.location.MeridianLocation;
import com.arubanetworks.meridian.location.MeridianOrientation;
import com.arubanetworks.meridian.maps.MapFragment;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            loadMapFragment();
        }
    }

    private void loadMapFragment() {

//        MapFragment.Builder builder = new MapFragment.Builder()
//                .setMapKey(EditorKey.forMap(BuildConfig.ArubaMapKey, BuildConfig.ArubaAppKey));
//        MapOptions mapOptions = MapOptions.getDefaultOptions();
//        // Turn off the overview button (only shown if there is an overview map for the location)
//        mapOptions.HIDE_OVERVIEW_BUTTON = true;
//        builder.setMapOptions(mapOptions);
//        // example: how to set placemark markers text size
//                /*
//                    MapOptions mapOptions = ((MapFragment) fragment).getMapOptions();
//                    mapOptions.setTextSize(14);
//                    builder.setMapOptions(mapOptions);
//                */
//        // example: how to start directions programmatically
//
//        final MapFragment mapFragment = builder.build();
//        mapFragment.setMapEventListener(new MapView.MapEventListener() {
//
//            @Override
//            public void onMapLoadFinish() {
//
//            }
//
//            @Override
//            public void onMapLoadStart() {
//
//            }
//
//            @Override
//            public void onPlacemarksLoadFinish() {
//                        /*for (Placemark placemark : mapFragment.getMapView().getPlacemarks()) {
//                            if ("APPLE".equals(placemark.getName())) {
//                                mapFragment.startDirections(DirectionsDestination.forPlacemarkKey(placemark.getKey()));
//                            }
//                        }*/
//            }
//
//            @Override
//            public void onMapRenderFinish() {
//
//            }
//
//            @Override
//            public void onMapLoadFail(Throwable tr) {
//                if (mapFragment.isAdded() && mapFragment.getActivity() != null) {
//                    String message = getString(com.arubanetworks.meridian.R.string.mr_error_invalid_map);
//                    if (tr != null) {
//                        if (tr instanceof VolleyError && ((VolleyError) tr).networkResponse != null && ((VolleyError) tr).networkResponse.statusCode == 401) {
//                            message = "HTTP 401 Error: Please verify the Editor token.";
//                        } else if (!Strings.isNullOrEmpty(tr.getLocalizedMessage())) {
//                            message = tr.getLocalizedMessage();
//                        }
//                    }
//                    new AlertDialog.Builder(mapFragment.getActivity())
//                            .setTitle(getString(com.arubanetworks.meridian.R.string.mr_error_title))
//                            .setMessage(message)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setPositiveButton(com.arubanetworks.meridian.R.string.mr_ok, null)
//                            .show();
//                }
//            }
//
//            @Override
//            public void onMapTransformChange(Matrix transform) {
//
//            }
//
//            @Override
//            public void onLocationUpdated(MeridianLocation location) {
//
//            }
//
//            @Override
//            public void onOrientationUpdated(MeridianOrientation orientation) {
//
//            }
//
//            @Override
//            public boolean onLocationButtonClick() {
//                // example of how to override the behavior of the location button
//                final MapView mapView = mapFragment.getMapView();
//                MeridianLocation location = mapView.getUserLocation();
//                if (location != null) {
//                    mapView.updateForLocation(location);
//
//                } else {
//                    LocationRequest.requestCurrentLocation(getApplicationContext(), EditorKey.forApp(BuildConfig.ArubaAppKey), new LocationRequest.LocationRequestListener() {
//                        @Override
//                        public void onResult(MeridianLocation location) {
//                            mapView.updateForLocation(location);
//                        }
//
//                        @Override
//                        public void onError(LocationRequest.ErrorType location) {
//                            // handle the error
//                        }
//                    });
//                }
//                return true;
//            }
//        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_map, new MapViewFragment())
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Begin monitoring for Aruba Beacon-based Campaign events
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // This odd delay thing is due to a bug with 23 currently.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                CampaignsService.startMonitoring(MainActivity.this, Application.APP_KEY);
////                selectItem(0);
                loadMapFragment();
            }
        },1000);
    }
}
