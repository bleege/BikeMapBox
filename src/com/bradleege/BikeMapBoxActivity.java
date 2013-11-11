package com.bradleege;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.bradleege.markers.MapBoxMarker;
import com.bradleege.markers.MapBoxMarkerSize;
import com.bradleege.tilesource.MapBoxTileSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BikeMapBoxActivity extends Activity
{
    private MapView mapView = null;
	private Handler handler = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Setup MapBox As Provider
//        MapBoxTileSource mapBoxTileSource = new MapBoxTileSource("mbtiles", ResourceProxy.string.base, 1, 20, 256, ".png", "bleege.map-3a5gfw2p");
		MapBoxTileSource mapBoxTileSource = new MapBoxTileSource("bleege.map-3a5gfw2p");

        // Interact With the MapView
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setTileSource(mapBoxTileSource);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(14);
        mapView.getController().setCenter(new GeoPoint(43.05277119900874, -89.42244529724121));

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				Log.i(getClass().getCanonicalName(), String.format("handler.handleMessage() called. Marker Loaded Key? = %s", msg.getData().containsKey("MARKERLOADED")));
				mapView.invalidate();
			}
		};
    }

	@Override
	protected void onStart()
	{
		super.onStart();
		// Load The Route
		new LoadRouteOntoMapTask().execute();

		// Load UW Arboretum Marker

		// Stock OSMDroid Marker
		//OverlayItem arbMarker = new OverlayItem("UW Arboretum", "Fields, Trees, Abandoned City, etc", new GeoPoint(43.04277119900874, -89.42544529724121));

		// MapBox Marker Support
		MapBoxMarker arbMarker = new MapBoxMarker("UW Arboretum", "Fields and Trees", new GeoPoint(43.038673562216715, -89.42789554595947), "park", null, MapBoxMarkerSize.LARGE, getResources(), handler);
		MapBoxMarker vilasMarker = new MapBoxMarker("Vilas Park", "Skating Rink, Sports Fields, Zoo", new GeoPoint(43.05519612238735, -89.41253185272217), "park", null, MapBoxMarkerSize.LARGE, getResources(), handler);

		DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
		ItemizedIconOverlay<OverlayItem> myItemizedIconOverlay  = new ItemizedIconOverlay<OverlayItem>(new ArrayList<OverlayItem>(), null, defaultResourceProxyImpl);
		myItemizedIconOverlay.addItem(arbMarker);
		myItemizedIconOverlay.addItem(vilasMarker);

		mapView.getOverlays().add(myItemizedIconOverlay);
	}

	private class LoadRouteOntoMapTask extends AsyncTask<Void, Void, ArrayList<GeoPoint>>
    {
        @Override
        protected ArrayList<GeoPoint> doInBackground(Void... params)
        {
            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();

            try
            {
                // Load Data From geojson file
                StringBuilder builder = new StringBuilder();

                InputStream is = getResources().getAssets().open("map.geojson");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }

                JSONObject json = new JSONObject(builder.toString());
                JSONArray coordinates = json.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");

                for (int lc = 0; lc < coordinates.length(); lc++)
                {
                    JSONArray c = coordinates.getJSONArray(lc);
                    GeoPoint gp = new GeoPoint(c.getDouble(1), c.getDouble(0));
                    points.add(gp);
                }
            }
            catch (IOException e)
            {
                Log.e(LoadRouteOntoMapTask.class.getName(), "Error Reading JSON Data", e);
            } catch (JSONException e)
            {
                Log.e(LoadRouteOntoMapTask.class.getName(), "Error Converting GeoJSON To GeoPoints", e);
            }

            return points;
        }

        @Override
        protected void onPostExecute(ArrayList<GeoPoint> geoPoints)
        {
            super.onPostExecute(geoPoints);

            // Build and Display the Path on the map
            PathOverlay myPath = new PathOverlay(Color.RED, getApplication().getApplicationContext());
            for (GeoPoint gp : geoPoints)
            {
                myPath.addPoint(gp);
            }
            mapView.getOverlays().add(0, myPath);
        }
    }
}
