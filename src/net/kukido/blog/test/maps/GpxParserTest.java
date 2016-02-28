package net.kukido.blog.test.maps;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.kukido.blog.test.TestConstants;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxParser;

import org.junit.*;
import static org.junit.Assert.*;

public class GpxParserTest 
{
	private static final String TEST_FILE = TestConstants.SAMPLE_FILES + "/big-bear-cougar-bertha.gpx";
	
	/**
	 * Ensures that a file without timestamps (for instance, a gpx of a planned route, rather than a recorded ride)
	 * doesn't cause NullPointerExceptions, etc.
	 */
	@Test
	public void test_parse_no_timestamps()
	{
		try {
			GpxParser parser = new GpxParser();
			File file = new File(TEST_FILE);
			FileInputStream in = new FileInputStream(file);
			List<GpsTrack> tracks = parser.parse(in);
			assertTrue(tracks.size() == 1);
			
			GpsTrack track = tracks.get(0);
			
			assertTrue(track.getStartTime().getTime() == 0); // There are no times in the source file. All timestamps should be zero.
			assertTrue(track.getFinishTime().getTime() == 0);
			assertTrue(track.getDuration() == 0);
			assertTrue(track.getFormattedDuration() != null); // I don't care what it returns. But it had better be *something*.
			assertTrue("Big Bear: Cougar Crest to Bertha Peak".equals(track.getName()));
		}
		catch (Exception e) {
			fail(e.toString());
		}
	}
}
