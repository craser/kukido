package net.kukido.blog.test.datamodel;

import java.util.Date;

import net.kukido.blog.datamodel.LocationMask;
import net.kukido.maps.GpsLocation;

import org.junit.*;
import static org.junit.Assert.*;

public class LocationMaskTest {
	
	@Test
	public void test_contains() {
		GpsLocation a = new GpsLocation() {
		    public double getMetersTo(GpsLocation b) {
		    	return 5;
		    }
		};
		GpsLocation b = new GpsLocation(0f, 0f, 0f, new Date());
		LocationMask mask = new LocationMask(a, 10);
		assertTrue(mask.contains(b));
		mask = new LocationMask(a, 3);
		assertFalse(mask.contains(b));
	}

}
