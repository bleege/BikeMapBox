/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 10/15/13 at 7:57 PM
 */

package com.bradleege.tilesource;

import android.util.Log;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class MapBoxTileSource extends OnlineTileSourceBase
{
    private String mapBoxMapId = null;

	private static String mapBoxBaseUrl = "http://api.tiles.mapbox.com/v3/";

	/**
	 * TileSource with configuration defaults set.
	 * @param mapBoxMapId MapBox Map Id
	 */
	public MapBoxTileSource(String mapBoxMapId)
	{
		super("mbtiles", ResourceProxy.string.base, 1, 20, 256, ".png");
		this.mapBoxMapId = mapBoxMapId;
	}

	/**
	 * TileSource allowing majority of options (sans url) to be user selected.
	 * @param name Name
	 * @param resourceId Resource Id
	 * @param zoomMinLevel Minimum Zoom Level
	 * @param zoomMaxLevel Maximum Zoom Level
	 * @param tileSizePixels Size of Tile Pixels
	 * @param imageFilenameEnding Image File Extension
	 * @param mapBoxMapId MapBox Map Id
	 */
	public MapBoxTileSource(String name, ResourceProxy.string resourceId, int zoomMinLevel, int zoomMaxLevel, int tileSizePixels, String imageFilenameEnding, String mapBoxMapId)
    {
        super(name, resourceId, zoomMinLevel, zoomMaxLevel, tileSizePixels, imageFilenameEnding, mapBoxBaseUrl);
        this.mapBoxMapId = mapBoxMapId;
    }

	/**
	 * TileSource allowing all options to be user selected.
	 * @param name Name
	 * @param resourceId Resource Id
	 * @param zoomMinLevel Minimum Zoom Level
	 * @param zoomMaxLevel Maximum Zoom Level
	 * @param tileSizePixels Size of Tile Pixels
	 * @param imageFilenameEnding Image File Extension
	 * @param mapBoxMapId MapBox Map Id
	 * @param mapBoxVersionBaseUrl MapBox Version Base Url @see https://www.mapbox.com/developers/api/#Versions
	 */
	public MapBoxTileSource(String name, ResourceProxy.string resourceId, int zoomMinLevel, int zoomMaxLevel, int tileSizePixels, String imageFilenameEnding, String mapBoxMapId, String mapBoxVersionBaseUrl)
	{
		super(name, resourceId, zoomMinLevel, zoomMaxLevel, tileSizePixels, imageFilenameEnding, mapBoxVersionBaseUrl);
		this.mapBoxMapId = mapBoxMapId;
	}

    public String getMapBoxMapId()
    {
        return mapBoxMapId;
    }

    public void setMapBoxMapId(String mapBoxMapId)
    {
        this.mapBoxMapId = mapBoxMapId;
    }

    @Override
    public String getTileURLString(MapTile mapTile)
    {
        StringBuffer url = new StringBuffer(getBaseUrl());
        url.append(getMapBoxMapId());
        url.append("/");
        url.append(mapTile.getZoomLevel());
        url.append("/");
        url.append(mapTile.getX());
        url.append("/");
        url.append(mapTile.getY());
        url.append(".png");

        String res = url.toString();
        Log.i(getClass().getCanonicalName(), "URL = '" + res  + "'");

        return res;
    }
}
