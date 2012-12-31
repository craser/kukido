package net.kukido.blog.test.maps;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.kukido.maps.GarminLogbookParser;
import net.kukido.maps.GpsTrack;

import org.junit.*;
import static org.junit.Assert.*;

public class GarminLogbookParserTest 
{
	private static final String TEST_FILE = "/Users/craser/Development/workspace/KukidoBlogTemp/sample-files/garmin-logbook-sample.xml";
	
	@Test
	public void test_parse()
	{
		try {
			GarminLogbookParser parser = new GarminLogbookParser();
			File file = new File(TEST_FILE);
			FileInputStream in = new FileInputStream(file);
			List<GpsTrack> tracks = parser.parse(in);
			assertTrue(tracks.size() == 1);
			
			GpsTrack track = tracks.get(0);
			assertTrue(track.size() == 3);
			
			assertTrue(track.get(0).getLatitude() == 60.084179f);
			assertTrue(track.get(0).getLongitude() == 23.679762f);
			assertTrue(track.get(0).getElevation() == 50.125977f);
			assertTrue(track.get(0).getHeartRate() == 43f);

			assertTrue(track.get(1).getLatitude() == 60.084154f);
			assertTrue(track.get(1).getLongitude() == 23.679851f);
			assertTrue(track.get(1).getElevation() == 52.048584f);
			assertTrue(track.get(1).getHeartRate() == 43f);

			assertTrue(track.get(2).getLatitude() == 60.084152f);
			assertTrue(track.get(2).getLongitude() == 23.679884f);
			assertTrue(track.get(2).getElevation() == 51.567871f);
			assertTrue(track.get(2).getHeartRate() == 43f);
			
		}
		catch (Exception e) {
			fail(e.toString());
		}
	}

}
