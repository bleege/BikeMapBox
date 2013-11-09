/**
 * @author Brad Leege <bleege@gmail.com>
 * Created on 11/8/13 at 2:58 PM
 */
package com.bradleege.markers;

public enum MapBoxMarkerSize
{
	SMALL("s"), MEDIUM("m"), LARGE("l");

	private String sizeString = null;

	private MapBoxMarkerSize(String sizeString)
	{
		this.sizeString = sizeString;
	}

	public String getSizeString()
	{
		return sizeString;
	}
}
