/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 11/8/13 at 2:19 PM
 */

package com.bradleege.markers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import java.io.IOException;

public class MapBoxMarker extends OverlayItem
{
	private static String markerURL = "http://api.tiles.mapbox.com/v3/marker/pin-%s%s%s%s.png";

	private String symbolName;
	private String colorHex;
	private MapBoxMarkerSize mapBoxMarkerSize;
	private Resources resources;

	private MapView mapView;

	public MapBoxMarker(String aTitle, String aSnippet, GeoPoint aGeoPoint, String symbolName, String colorHex, MapBoxMarkerSize markerSize, Resources resources, MapView theMap)
	{
		super(aTitle, aSnippet, aGeoPoint);

		// TODO - Implement Event Listener for redrawing mapView instead of hardwiring reference into each marker
		this.mapView = theMap;

		// Symbol Names From Maki Project - https://www.mapbox.com/maki/
		if (symbolName != null)
		{
			this.symbolName = symbolName;
		}
		else
		{
			this.symbolName = "star";
		}

		if (colorHex != null)
		{
			this.colorHex = colorHex.replace("#", "");
		}
		else
		{
			this.colorHex = "ff0000";
		}

		if (markerSize != null)
		{
			this.mapBoxMarkerSize = markerSize;
		}
		else
		{
			this.mapBoxMarkerSize = MapBoxMarkerSize.MEDIUM;
		}

		this.resources = resources;

		// "Retina Display"
		String retina = "";
		if (resources.getDisplayMetrics().densityDpi > 300)
		{
			retina = "@2x";
		}

		// Load Marker Image
		String url = String.format(markerURL, new Object[]{mapBoxMarkerSize.getSizeString(), "-" + this.symbolName, "+" + this.colorHex, retina});

		Drawable d = MapBoxMarkerCache.getInstance().get(url);
		Log.i(getClass().getCanonicalName(), "d = " + d);
		if (d != null)
		{
			setMarker(d);
		}
		else
		{
			new DownloadMarkerTask().execute(url);
		}

		new DownloadMarkerTask().execute(url);
	}

	private class DownloadMarkerTask extends AsyncTask<String, Void, byte[]>
	{
		private String url;

		@Override
		protected byte[] doInBackground(String... urls)
		{
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
			url = urls[0];
			Log.i(getClass().getCanonicalName(), "Image URL = " + url);
			try
			{
				HttpResponse response = httpClient.execute(new HttpGet(url));

				if (response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity entity = response.getEntity();
					return EntityUtils.toByteArray(entity);
				}
				else
				{
					throw new IOException("Marker Download Failed.  Status Code: " + response.getStatusLine().getStatusCode() + "; Reason: " + response.getStatusLine().getReasonPhrase());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				httpClient.close();
			}
			return new byte[0];
		}

		protected void onPostExecute(byte[] result)
		{
			Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
			Drawable d = new BitmapDrawable(resources, bitmap);
			Log.i(getClass().getCanonicalName(), "onPostExecute()... d = " + d);
			setMarker(d);

			if (!MapBoxMarkerCache.getInstance().keyExists(url))
			{
				Log.i(getClass().getCanonicalName(), "key for URL" + url + " doesn't exist so adding it.");
				MapBoxMarkerCache.getInstance().add(url, d);
			}

			// Force the map to redraw the new marker
			mapView.invalidate();
		}
	}
}
