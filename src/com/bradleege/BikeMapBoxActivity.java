package com.bradleege;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.bradleege.tilesource.MapBoxTileSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BikeMapBoxActivity extends Activity
{
    private MapView mapView = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Setup MapBox As Provider
        MapBoxTileSource mapBoxTileSource = new MapBoxTileSource("mbtiles", ResourceProxy.string.base, 1, 20, 256, ".png", "http://api.tiles.mapbox.com/v3/");

        // Interact With the MapView
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setTileSource(mapBoxTileSource);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(14);
        mapView.getController().setCenter(new GeoPoint(43.05277119900874, -89.42244529724121));

        // Load The Route
        new LoadRouteOntoMapTask().execute();
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
            mapView.getOverlays().add(myPath);
        }
    }
}
