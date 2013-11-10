/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 11/9/13 at 4:16 PM
 */

package com.bradleege.markers;

import android.graphics.drawable.Drawable;
import java.util.HashMap;

public class MapBoxMarkerCache
{
	private static MapBoxMarkerCache ourInstance = new MapBoxMarkerCache();

	private HashMap<String, Drawable> cache;

	public static MapBoxMarkerCache getInstance()
	{
		return ourInstance;
	}

	private MapBoxMarkerCache()
	{
		cache = new HashMap<String, Drawable>();
	}

	public Boolean keyExists(String key)
	{
		return cache.containsKey(key.toLowerCase());
	}

	public void add(String key, Drawable marker)
	{
		cache.put(key.toLowerCase(), marker);
	}

	public Drawable get(String key)
	{
		return cache.get(key.toLowerCase());
	}

	public void remove(String key)
	{
		cache.remove(key.toLowerCase());
	}

	public void clearCache()
	{
		cache.clear();
	}
}
