package net.kukido.maps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GpxFormatter
{
    static private final SimpleDateFormat gpxDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszz");
    
    static public void main(String... args)
        throws Exception
    {
        // Test this class, since I evidently started this branch from 
        // before I added automated testing.  Bah.
        
        List<GpsTrack> tracks = buildTestGpsTracks(5);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new GpxFormatter().format(tracks, out);
        String gpx = out.toString();
        System.out.println("done formatting gpx");
        System.out.println("---");
        System.out.println(gpx);
        System.out.println("---");
        
        GpxParser parser = new GpxParser();
        List<GpsTrack> parsed = parser.parse(gpx.getBytes());
        
        out = new ByteArrayOutputStream();
        new GpxFormatter().format(parsed, out);
        String newGpx = out.toString();
        
        System.out.println(newGpx.equals(gpx));
        
        
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
    
    public void format(List<GpsTrack> tracks, OutputStream o) throws IOException
    {
        PrintWriter out = null;
        try {
            out = new PrintWriter(o);
            formatAll(tracks, out);
            out.flush();
            
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            try { out.close(); } catch (Exception ignored) {}
        }
        
    }
    
    private void formatAll(List<GpsTrack> tracks, PrintWriter out) throws IOException
    {
        printHeader(out);
        printGpx(tracks, out);    
    }
    
    private void printHeader(PrintWriter out)
    {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        out.println(header);
    }
    
    private void printGpx(List<GpsTrack> tracks, PrintWriter out) 
    {
        String open_tag = "<gpx"
                + " version=\"1.0\""
                + " creator=\"GPSBabel - http://www.gpsbabel.org\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xmlns=\"http://www.topografix.com/GPX/1/0\""
                + " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd\">";
        String close_tag = "</gpx>";
        
        out.println(open_tag);
        for (GpsTrack track : tracks) {
            printTrack(track, out);
        }
        out.println(close_tag);
    }
    
    private void printTrack(GpsTrack track, PrintWriter out)
    {
        out.println("<trk>");
        out.println("<!-- NOT including 'name' element. -->");
        out.println("<!-- NOT including 'number' element. -->");
        // FIXME: Making the whole thing into one trackseg, regardless 
        // of original segmentatation.
        out.println("<trkseg>");
        String locFormat = "<trkpt lat=\"{0}\" lon=\"{1}\">"
                + "\n<ele>{2}</ele>"
                + "\n<time>{3}</time>" // 2012-08-12T17:35:12Z
                + "\n</trkpt>";
        for (GpsLocation loc : track) {
            String timestamp = gpxDateFormat.format(loc.getTimestamp());
            String logString = MessageFormat.format(locFormat,
                    loc.getLatitude(),
                    loc.getLongitude(),
                    loc.getElevation(),
                    timestamp);
            out.println(logString);
        }
        out.println("</trkseg>");
        out.println("</trk>");
    }

}
