package net.kukido.blog.test.maps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.kukido.maps.*;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * I need to be able to parse TCX, convert it to GPX, and then parse that GPX at a
 * later date. This tests that data format "pipeline".
 */
public class GpsPipelineTest
{
    private static final String TEST_FILE = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/garmin-sample.tcx";
    private static final String TEST_TCX_INPUT = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/garmin-doc.tcx";
    private static final String TEST_GPX_INPUT = "/Users/craser/Development/workspace_intellij14/kukido/sample-files/garmin-doc.gpx";

    /**
     * Ensures that a file without timestamps (for instance, a gpx of a planned route, rather than a recorded ride)
     * doesn't cause NullPointerExceptions, etc.
     */
    @Test
    public void test_pipeline()
    {
        try {
            File file = new File(TEST_FILE);
            FileInputStream in = new FileInputStream(file);

            TcxParser tcxParser = new TcxParser();
            List<GpsTrack> inTracks = tcxParser.parse(in);

            GpxFormatter gpxFormatter = new GpxFormatter();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            gpxFormatter.format(inTracks, baos);

            GpxParser gpxParser = new GpxParser();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            List<GpsTrack> outTracks = gpxParser.parse(bais);

            assertEquals(inTracks.size() + " != " + outTracks.size(), inTracks.size(), outTracks.size());
            for (int i = 0; i < inTracks.size(); i++) {
                GpsTrack inTrack = inTracks.get(i);
                GpsTrack outTrack = outTracks.get(i);

                assertEquals(inTrack.getName(), outTrack.getName());
                assertEquals(inTrack.size(), outTrack.size());

                for (int j = 0; j < inTrack.size(); j++) {
                    GpsLocation a = inTrack.get(j);
                    GpsLocation b = outTrack.get(j);
                    assertTrue(a + " != " + b, a.equals(b));
                }
            }

        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Check that the translation from TCX to GPX is solid by parsing the input and the output and comparing.
     */
    @Test
    public void test_file_equivalence()
    {
        try {
            FileInputStream tcxIn = new FileInputStream(new File(TEST_TCX_INPUT));
            List<GpsTrack> tcx = new TcxParser().parse(tcxIn);

            FileInputStream gpxIn = new FileInputStream(new File(TEST_GPX_INPUT));
            List<GpsTrack> gpx = new GpxParser().parse(gpxIn);

            assertEquals(tcx.size(), gpx.size());

            for (int i = 0; i < tcx.size(); i++) {
                GpsTrack tcxTrack = tcx.get(i);
                GpsTrack gpxTrack = gpx.get(i);
                assertEquals("Tracks at " + i + " not equal in length.", tcxTrack.size(), gpxTrack.size());
                for (int j = 0; j < tcxTrack.size(); j++) {
                    GpsLocation t = tcxTrack.get(i);
                    GpsLocation g = tcxTrack.get(i);
                    assertTrue("Empty latitude.", t.getLatitude() != 0f);
                    assertTrue("Empty longitude.", t.getLongitude() != 0f);
                    assertTrue(t + " != " + g, t.equals(g));
                }
            }
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

}
