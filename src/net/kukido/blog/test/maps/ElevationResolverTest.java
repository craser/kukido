package net.kukido.blog.test.maps;

import java.io.IOException;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.google.ElevationResolver;

import org.junit.* ;
import org.xml.sax.SAXException;

import static org.junit.Assert.* ;

/**
 * Tests the ElevationResolver.
 * 
 * @author craser
 *
 */
public class ElevationResolverTest 
{
	/**
	 * Tests elevation resolution, checking that the returned values
	 * match the sample data provided in the API docs:
	 * 
	 * https://developers.google.com/maps/documentation/elevation/#LineElevation
	 * @throws SAXException 
	 * @throws IOException 
	 */
	@Test
	public void test_resolve() throws IOException, SAXException
	{
		/*
		 *    "results" : [
      {
         "elevation" : 4411.941894531250,
         "location" : {
            "lat" : 36.5785810,
            "lng" : -118.2919940
         },
         "resolution" : 19.08790397644043
      },
      {
         "elevation" : 1381.861694335938,
         "location" : {
            "lat" : 36.41150289067028,
            "lng" : -117.5602607523847
         },
         "resolution" : 19.08790397644043
      },
      {
         "elevation" : -84.61699676513672,
         "location" : {
            "lat" : 36.239980,
            "lng" : -116.831710
         },
         "resolution" : 19.08790397644043
      }
		 */
		float[][] gps = {
				// lat, lon, ele
				{ 36.5785810f, -118.2919940f, 4411.941894531250f },
				{ 36.41150289067028f, -117.5602607523847f, 1381.861694335938f },
				{36.239980f, -116.831710f, -84.61699676513672f }
		};
		GpsTrack track = new GpsTrack();
		for (float[] p : gps) {
			GpsLocation l = new GpsLocation();
			l.setLatitude(p[0]);
			l.setLongitude(p[1]);
			track.add(l);
		}
		
		ElevationResolver resolver = new ElevationResolver();
		track = resolver.resolve(track);
		
		assertTrue(track.size() == gps.length);
		for (int i = 0; i < gps.length; i++) {
			GpsLocation l = track.get(i);
			float[] p = gps[i];
			assertTrue(withinRange(l.getElevation(), p[2], 5f));
		}
	}
	
	/**
	 * Is b within r of a?
	 * @return
	 */
	private boolean withinRange(float a, float b, float r)
	{
		return b > (a - r) || b < (a + r);
	}

}
