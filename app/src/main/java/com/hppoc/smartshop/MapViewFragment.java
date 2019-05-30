package com.hppoc.smartshop;

import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.location.MeridianLocation;
import com.arubanetworks.meridian.location.MeridianOrientation;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;
import com.arubanetworks.meridian.maps.Marker;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapViewFragment extends Fragment implements MapView.DirectionsEventListener, MapView.MapEventListener {

    public MapViewFragment() {
    }

    /**
     * Demonstrates the use of the MapView.  It is recommended to use the SDK map fragment instead of the mapview
     */

    private MapView mapView;

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
        mapOptions.HIDE_DIRECTIONS_CONTROLS = true;
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
    }

    @Override
    public void onMapLoadFail(Throwable tr) {
    }

    @Override
    public void onMapRenderFinish() {
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
        return false;
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
}
