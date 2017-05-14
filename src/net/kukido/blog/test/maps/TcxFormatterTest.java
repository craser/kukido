package net.kukido.blog.test.maps;

import java.io.PrintStream;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import net.kukido.blog.dataaccess.AttachmentDao;
import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.datamodel.Attachment;
import net.kukido.maps.*;

import java.util.List;

import org.junit.* ;
import static org.junit.Assert.* ;

/**
 * Created by craser on 11/6/15.
 */
public class TcxFormatterTest
{
    @Test
    public void testDateFormat() {
        Date date = new Date();
        date.setTime(1446864999000l); // Fri Nov 06 18:56:39 PST 2015

        TcxFormatter formatter = new TcxFormatter();
        String formatted = formatter.formatDate(date);

        assertEquals("2015-11-06T18:56:39-08:00", formatted);
    }

//    @Test
//    public void outputSampleTrack() {
//        try {
//            TcxFormatter formatter = new TcxFormatter();
//            List<GpsTrack> tracks = buildTestGpsTracks(3);
//
//            System.out.println("Track:");
//            formatter.format(tracks, System.out);
//        }
//        catch (Exception e) {
//            fail("Exception: " + e);
//        }
//    }

    @Test
    public void testRendering() {
        try {
            String fileName = "20150904-weir-cyn.gpx";
            AttachmentDao dao = new AttachmentDao();
            Attachment attachment = dao.findByFileName(fileName);
            dao.populateBytes(attachment);
            List<GpsTrack> tracks = new GpxParser().parse(attachment.getBytes());
            TcxFormatter formatter = new TcxFormatter();

            PrintStream out = new PrintStream(new ByteArrayOutputStream()); // Just somewhere to dump the result.
            formatter.format(tracks, out);
        }
        catch (Exception e) {
            fail("Error: " + e);
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
