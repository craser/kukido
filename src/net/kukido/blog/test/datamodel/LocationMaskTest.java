package net.kukido.blog.test.datamodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.kukido.blog.datamodel.Location;
import net.kukido.blog.datamodel.LocationMask;
import net.kukido.blog.test.TestConstants;
import net.kukido.maps.GpsLocation;

import net.kukido.maps.GpsTrack;
import net.kukido.maps.GpxParser;
import org.junit.*;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class LocationMaskTest {

	/**
	 * I've purposely picked this sample file because I set out from the Maybury trailhead and
	 * crossed the top of Coachwhip, and did NOT travel through the heart of Disneyland.
	 */
	private static final String TEST_FILE = TestConstants.SAMPLE_FILES + "/20150803-coachwhip.gpx";


	@Test
	public void test_contains() {
		try {
			LocationMask mask = new LocationMask();
			GpsTrack track = getSampleTrack();
			int initialSize = track.size();
			assertTrue("Sanity check on initial track: should not be zero-length.", initialSize != 0);

			int zeroMaskSize = mask.mask(track).size();
			assertTrue("No Locations should not change the size of the track.", zeroMaskSize == initialSize);

			mask.add(getMayburyTrailhead());
			int oneMaskSize = mask.mask(track).size();
			assertTrue("Trailhead Location should reduce size of track.", oneMaskSize < initialSize);

			mask.add(getCoachwhipTop());
			int twoMaskSize = mask.mask(track).size();
			assertTrue("Coachwhip Location should further reduce track size.", twoMaskSize < oneMaskSize);

			mask.add(getNowhere());
			int threeMaskSize = mask.mask(track).size();
			assertTrue("Disneyland Location should NOT change the track size.", threeMaskSize == twoMaskSize);

		}
		catch (Exception e) {
			fail(e.toString());
		}
	}

	public Location getCoachwhipTop() {
		return new Location(33.822800f, -117.754950f, 20);
	}

	public Location getMayburyTrailhead() {
		return new Location(33.818376f, -117.783446f, 100);
	}

	public Location getNowhere() {
		// Middle of Disneyland.
		return new Location(33.812015f, -117.918974f, 500);
	}

	/**
	 * Ensures that a file without timestamps (for instance, a gpx of a planned route, rather than a recorded ride)
	 * doesn't cause NullPointerExceptions, etc.
	 */
	public GpsTrack getSampleTrack() throws SAXException, IOException {
		GpxParser parser = new GpxParser();
		File file = new File(TEST_FILE);
		FileInputStream in = new FileInputStream(file);
		List<GpsTrack> tracks = parser.parse(in);
		GpsTrack track = tracks.get(0);
		return track;
	}

}
