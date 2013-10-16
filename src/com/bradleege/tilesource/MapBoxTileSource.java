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
    public MapBoxTileSource(String aName, ResourceProxy.string aResourceId, int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding, String... aBaseUrl)
    {
        super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding, aBaseUrl);
    }

    @Override
    public String getTileURLString(MapTile mapTile)
    {
        StringBuffer url = new StringBuffer("http://api.tiles.mapbox.com/v3/bleege.map-3a5gfw2p/");
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
