package net.kukido.maps.google;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.SAXException;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;

/**
 * Takes a GpsTrack and calls out to Google's elevation service
 * to correct elevation data.  (Satellite data used by GPS units
 * has intentionally-induced chatter in elevation.)
 * @author craser
 *
 */
public class ElevationResolver 
{
	public GpsTrack resolve(GpsTrack track) throws IOException, SAXException 
	{
		ElevationResponseParser parser = new ElevationResponseParser();
		URL url = buildUrl(track);
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		ElevationResponse response = parser.parse(in);
		GpsTrack resolved = response.fixElevations(track);
		
		return resolved;
	}
	
	private URL buildUrl(GpsTrack track) throws MalformedURLException
	{
		StringBuffer url = new StringBuffer("http://maps.googleapis.com/maps/api/elevation/xml?locations=");
		for (GpsLocation loc : track) {
			url.append(loc.getLatitude());
			url.append(",");
			url.append(loc.getLongitude()).append("|"); // Don't forget to strip off the last one.
		}
		
		return new URL(url.substring(0, url.length() - 1));
	}

}
