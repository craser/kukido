package net.kukido.blog.datamodel;

import java.util.ArrayList;
import java.util.Collection;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;

public class LocationMask extends Location {

	private Collection<Location> masks;
	
	public LocationMask() {
		this(new ArrayList<Location>());
	}
	
	public LocationMask(Collection<Location> masks) {
		this.masks = masks;
	}
	
	public void add(Location mask) {
		masks.add(mask);
	}
	
	public boolean contains(GpsLocation loc) {
		for (Location mask : masks) {
			if (mask.contains(loc)) {
				return true;
			}
		}
		return false;
	}
	
	public Collection<Location> getLocations() {
		return masks;
	}
	
	public GpsTrack mask(GpsTrack track) {
		GpsTrack t = new GpsTrack();
		for (GpsLocation loc : track) {
			if (this.contains(loc)) {
				continue;
			}
			else {
				t.add(loc);
			}
		}
		return t;
	}
}
