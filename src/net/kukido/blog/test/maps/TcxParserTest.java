package net.kukido.blog.test.maps;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxParser;

import net.kukido.maps.TcxParser;
import org.junit.*;
import static org.junit.Assert.*;

public class TcxParserTest
{
    private static final String TEST_FILE = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/garmin-sample.tcx";
    private static final String TEST_TCX_INPUT = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/garmin-doc.tcx";

    /**
     * Ensures that a file without timestamps (for instance, a gpx of a planned route, rather than a recorded ride)
     * doesn't cause NullPointerExceptions, etc.
     */
    @Test
    public void test_parse()
    {
        try {
            TcxParser parser = new TcxParser();
            File file = new File(TEST_FILE);
            FileInputStream in = new FileInputStream(file);
            List<GpsTrack> tracks = parser.parse(in);
            assertTrue("Found " + tracks.size() + " tracks. Expected 1.", tracks.size() == 1);

            GpsTrack track = tracks.get(0);

            assertTrue(track.size() == 5);
            assertTrue(track.getStart().getLatitude() == 33.8183600f);
            assertTrue(track.getStart().getLongitude() == -117.7840471f);
            assertTrue(track.getStart().getElevation() == 123.0000000f);
            assertTrue(track.getEnd().getLatitude() == 33.8183522f);
            assertTrue(track.getEnd().getLongitude() == -117.7840359f);
            assertTrue(track.getEnd().getElevation() == 123.5999756f);
            assertTrue("2015-12-06T17:20:40Z".equals(track.getName()));
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_parse_gps_unit_output()
    {
        try {
            FileInputStream in = new FileInputStream(new File(TEST_TCX_INPUT));
            List<GpsTrack> tracks = new TcxParser().parse(in);
            assertEquals("Should only be one track. Found: " + tracks.size(), tracks.size(), 1);
            GpsTrack track = tracks.get(0);
            assertEquals("Size should be 1758. Found: " + track.size(), track.size(), 1758);

            for (GpsLocation loc : track) {
                assertTrue(loc.getLatitude() != 0f);
                assertTrue(loc.getLongitude() != 0f);
            }
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

}
