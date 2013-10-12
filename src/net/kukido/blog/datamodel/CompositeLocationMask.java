package net.kukido.blog.datamodel;

import java.util.ArrayList;
import java.util.Collection;

import net.kukido.maps.GpsLocation;

public class CompositeLocationMask extends LocationMask {

	private Collection<LocationMask> masks;
	
	public CompositeLocationMask() {
		this(new ArrayList<LocationMask>());
	}
	
	public CompositeLocationMask(Collection<LocationMask> masks) {
		this.masks = masks;
	}
	
	public void add(LocationMask mask) {
		masks.add(mask);
	}
	
	public boolean contains(GpsLocation loc) {
		for (LocationMask mask : masks) {
			if (mask.contains(loc)) {
				return true;
			}
		}
		return false;
	}
}
