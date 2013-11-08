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


    public MapBoxTileSource(String aName, ResourceProxy.string aResourceId, int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding, String mapBoxMapId, String... aBaseUrl)
    {
        super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding, "http://api.tiles.mapbox.com/v3/");
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
