package net.kukido.blog.test.maps;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxFormatter;
import net.kukido.maps.GpxParser;
import java.util.List;

import org.junit.* ;
import static org.junit.Assert.* ;


public class GpxFormatterTest 
{
    private static final String TEST_GPX_INPUT = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/garmin-doc.gpx";
    private static final String TEST_STRAVA_INPUT = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/strava-output.gpx";

	@Test
    public void main()
    {
		try {
	        // Test this class, since I evidently started this branch from 
	        // before I added automated testing.  Bah. 
	        
	        List<GpsTrack> tracks = buildTestGpsTracks(5);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        new GpxFormatter().format(tracks, out);
	        String gpx = out.toString();
	        
	        GpxParser parser = new GpxParser();
	        List<GpsTrack> parsed = parser.parse(gpx.getBytes());
	        
	        out = new ByteArrayOutputStream();
	        new GpxFormatter().format(parsed, out);
	        String newGpx = out.toString();
	        
	        assertTrue(newGpx.equals(gpx));
		}
		catch (Exception e) {
			fail("Exception: " + e);
		}
    }

    @Test
    public void test_parse_formatter_output()
    {
        try {
            FileInputStream in = new FileInputStream(new File(TEST_GPX_INPUT));
            List<GpsTrack> tracks = new GpxParser().parse(in);
            assertEquals(tracks.size(), 1);
            GpsTrack track = tracks.get(0);
            assertEquals(1758, track.size());
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_strava_compare()
    {
        try {
            GpxParser parser = new GpxParser();
            FileInputStream gpxIn = new FileInputStream(new File(TEST_GPX_INPUT));
            List<GpsTrack> gpxTracks = parser.parse(gpxIn);
            GpsTrack gpxTrack = gpxTracks.get(0);

            FileInputStream stravaIn = new FileInputStream(new File(TEST_STRAVA_INPUT));
            List<GpsTrack> stravaTracks = parser.parse(stravaIn);
            GpsTrack stravaTrack = stravaTracks.get(0);

            assertEquals(stravaTrack.size() + " != " + gpxTrack.size(), stravaTrack.size(), gpxTrack.size());

            for (int i = 0; i < stravaTrack.size(); i++) {
                GpsLocation g = gpxTrack.get(i);
                GpsLocation s = stravaTrack.get(i);
                assertTrue(g + " != " + s, g.equals(s));
            }
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_thinned_track()
    {
        try {
            GpxParser parser = new GpxParser();
            FileInputStream gpxIn = new FileInputStream(new File(TEST_GPX_INPUT));
            List<GpsTrack> gpxTracks = parser.parse(gpxIn);
            GpsTrack gpxTrack = gpxTracks.get(0);

            GpsTrack thinned = gpxTrack.getThinnedTrack();
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

    static private List<GpsTrack> buildTestGpsTracks(int n)
    {
        GpsTrack track = new GpsTrack();
        for (float i = 0; i < n; i++) {
            float lat = i + 0.1f;
            float lon = i + 0.2f;
            float ele = i + 0.3f;
            Date time = new Date((long)i);
            GpsLocation loc = new GpsLocation(lat, lon, ele, time);
            track.add(loc);
        }

        List<GpsTrack> tracks = new ArrayList<GpsTrack>(1);
        tracks.add(track);
        return tracks;
    }



}
