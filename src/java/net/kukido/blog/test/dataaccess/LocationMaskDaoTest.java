package net.kukido.blog.test.dataaccess;

import net.kukido.blog.dataaccess.DataAccessException;
import net.kukido.blog.dataaccess.LocationMaskDao;
import net.kukido.blog.datamodel.Location;
import net.kukido.blog.datamodel.LocationMask;

import org.junit.*;
import static org.junit.Assert.*;

public class LocationMaskDaoTest {
	
	@Test
	public void test_findByUserId() throws DataAccessException {
		LocationMaskDao dao = new LocationMaskDao();
		LocationMask mask = dao.findByUserId(1);
		Location loc = mask.getLocations().iterator().next();
		assertTrue(loc != null);
		assertTrue(loc.getCenter().getLatitude() == 34.277048f);
		assertTrue(loc.getCenter().getLongitude() == -118.772365f);
	}

}
