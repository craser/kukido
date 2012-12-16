package net.kukido.blog.test.maps;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxFormatter;
import net.kukido.maps.GpxParser;
import java.util.List;

public class GpxFormatterTest 
{

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
	

}
