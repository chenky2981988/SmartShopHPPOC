package com.hppoc.smartshop;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.editor.Placemark;
import com.arubanetworks.meridian.location.MeridianLocation;
import com.arubanetworks.meridian.location.MeridianOrientation;
import com.arubanetworks.meridian.maps.MapFragment;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;
import com.arubanetworks.meridian.maps.Marker;
import com.arubanetworks.meridian.maps.directions.DirectionsDestination;
import com.arubanetworks.meridian.maps.directions.DirectionsSource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapViewFragment extends Fragment implements MapView.DirectionsEventListener, MapView.MapEventListener {

    String eventKey, itemKey;

    public MapViewFragment() {
    }

    /**
     * Demonstrates the use of the MapView.  It is recommended to use the SDK map fragment instead of the mapview
     */

    private MapView mapView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventKey = getArguments().getString("EVENT", "SHOW_MAP");
        itemKey = getArguments().getString("ITEM_KEY", "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = rootView.findViewById(R.id.demo_mapview);

        mapView.setAppKey(EditorKey.forApp(BuildConfig.ArubaAppKey));

        // If you want to handle MapView events
        mapView.setMapEventListener(this);

        // If you want to handle directions events
        mapView.setDirectionsEventListener(this);

        // If you want to handle marker events
        //mapView.setMarkerEventListener(this);

        // Set map options if desired
        MapOptions mapOptions = mapView.getOptions();
        mapOptions.HIDE_MAP_LABEL = true;
        //mapOptions.HIDE_DIRECTIONS_CONTROLS = true;
        mapOptions.HIDE_OVERVIEW_BUTTON = true;
        mapOptions.HIDE_ACCESSIBILITY_BUTTON = true;
        mapOptions.HIDE_LEVELS_CONTROL = true;
        mapOptions.HIDE_LOCATION_BUTTON = true;
        mapView.setOptions(mapOptions);

        // If you want to load a map other than the default one
        // It is recommended to do this after setting the map options
        mapView.setMapKey(EditorKey.forMap(BuildConfig.ArubaMapKey, BuildConfig.ArubaAppKey));

        // Demonstration of how to customize the mapView's locationMarker:
        //    change default color for Bluetooth to orange
        //    modify the name
        //    modify the details
        //    alternatively... hide the call-out entirely
        //
        // LocationMarker lm = mapView.getLocationMarker();
        // lm.setCustomColor(LocationMarker.State.BLUETOOTH, 0xffff7700);
        // lm.setName("Current Location Label");
        // lm.setDetails("Details");
        // // lm.setShowsCallout(false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up memory.
        mapView.onDestroy();
    }

    //
    // MapViewListener methods
    //
    @Override
    public void onMapLoadStart() {
    }

    @Override
    public void onMapLoadFinish() {

    }

    @Override
    public void onPlacemarksLoadFinish() {
        if (eventKey.equalsIgnoreCase("NAVIGATE")) {
            showDirection();
        }
    }

    @Override
    public void onMapLoadFail(Throwable tr) {
    }

    @Override
    public void onMapRenderFinish() {
        Log.d("TAG", "App Key : [" + mapView.getAppKey().toString() + "] Map Key : [" + mapView.getMapKey().toString() + "]");
    }

    @Override
    public void onMapTransformChange(Matrix transform) {
    }

    @Override
    public void onLocationUpdated(MeridianLocation location) {
    }

    @Override
    public void onOrientationUpdated(MeridianOrientation orientation) {
    }

    @Override
    public boolean onLocationButtonClick() {
//        List<Placemark> placemarkList = mapView.getPlacemarks();
//        DirectionsDestination directionsDestination;
//        DirectionsSource directionsSource;
//
//        if(placemarkList.size() >= 2){
//            //directionsSource = DirectionsSource.forPlacemarkKey(placemarkList.get(0).getKey());
//
//            PointF pointF = new PointF();
//            pointF.set(1150.0f,3000.0f);
//            directionsSource = DirectionsSource.forMapPoint(EditorKey.forMap(BuildConfig.ArubaMapKey, BuildConfig.ArubaAppKey),pointF);
//            Log.d("TAG","PlaceMark Key : " + placemarkList.get(0).getKey());
//            directionsDestination = DirectionsDestination.forPlacemarkKey(placemarkList.get(0).getKey());
//            //directionsDestination = DirectionsDestination.
//            MapOptions mapOptions = mapView.getOptions();
//            //mapOptions.HIDE_MAP_LABEL = true;
//            //mapOptions.HIDE_DIRECTIONS_CONTROLS = true;
//           // mapOptions.HIDE_OVERVIEW_BUTTON = true;
//            mapOptions.HIDE_ACCESSIBILITY_BUTTON = true;
//           // mapOptions.HIDE_LEVELS_CONTROL = true;
//            mapOptions.HIDE_LOCATION_BUTTON = true;
//
//            MapFragment mapDirFragment = new MapFragment.Builder()
//                    .setAppKey(mapView.getAppKey())
//                    .setMapKey(mapView.getMapKey())
//                    .setSource(directionsSource)
//                    .setMapOptions(mapOptions)
//                    .setDestination(directionsDestination)
//                    .build();
//
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.container_map, mapDirFragment)
//                    //.addToBackStack(null)
//                    .commit();
//
//        }
        showDirection();
        return true;
    }

    //
    // DirectionsEventListener methods
    //
    @Override
    public void onDirectionsReroute() {
    }

    @Override
    public boolean onDirectionsClick(Marker marker) {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Directions not implemented here.")
                    .setPositiveButton("OK", null)
                    .show();
        }
        return true;
    }

    @Override
    public boolean onDirectionsStart() {
        return false;
    }

    @Override
    public boolean onRouteStepIndexChange(int index) {
        return false;
    }

    @Override
    public boolean onDirectionsClosed() {
        return false;
    }

    @Override
    public boolean onDirectionsError(Throwable tr) {
        return false;
    }

    @Override
    public void onUseAccessiblePathsChange() {
    }

    private void showDirection() {
        List<Placemark> placemarkList = mapView.getPlacemarks();
        DirectionsDestination directionsDestination;
        DirectionsSource directionsSource;

        if (placemarkList.size() >= 2) {
            //directionsSource = DirectionsSource.forPlacemarkKey(placemarkList.get(0).getKey());
            for (int i = 0; i < placemarkList.size(); i++) {
                Placemark placemark = placemarkList.get(i);
                Log.d("TAG","placemark Name : " + placemark.getName());
                if (placemark.getName().toLowerCase().contains(itemKey.toLowerCase())) {
                    PointF pointF = new PointF();
                    //pointF.set(1150.0f, 3000.0f); //Near Door beacon
                    pointF.set(300.0f, 300.0f); // Upper left route point
                    directionsSource = DirectionsSource.forMapPoint(EditorKey.forMap(BuildConfig.ArubaMapKey, BuildConfig.ArubaAppKey), pointF);
                    Log.d("TAG", "PlaceMark Key : " + placemark.getKey());
                    directionsDestination = DirectionsDestination.forPlacemarkKey(placemark.getKey());
                    //directionsDestination = DirectionsDestination.
                    MapOptions mapOptions = mapView.getOptions();
//                    //mapOptions.HIDE_MAP_LABEL = true;
//                    mapOptions.HIDE_DIRECTIONS_CONTROLS = true;
//                    // mapOptions.HIDE_OVERVIEW_BUTTON = true;
//                    mapOptions.HIDE_ACCESSIBILITY_BUTTON = true;
//                    // mapOptions.HIDE_LEVELS_CONTROL = true;
//                    mapOptions.HIDE_LOCATION_BUTTON = true;

                    MapFragment mapDirFragment = new MapFragment.Builder()
                            .setAppKey(mapView.getAppKey())
                            .setMapKey(mapView.getMapKey())
                            .setSource(directionsSource) // Comment this line to enable live navigation/tracking
                            .setMapOptions(mapOptions)
                            .setDestination(directionsDestination)
                            .build();

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_map, mapDirFragment)
                            //.addToBackStack(null)
                            .commit();
                    break;
                }
            }


        }
    }
}
