package com.bradleege;

import android.app.Activity;
import android.os.Bundle;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class BikeMapBoxActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Interact With the MapView
        MapView mapView = (MapView)findViewById(R.id.mapview);
/*
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
*/
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(new GeoPoint(43.05277119900874, -89.42244529724121));

    }
}
