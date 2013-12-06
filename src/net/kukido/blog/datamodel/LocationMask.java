package net.kukido.blog.datamodel;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;

public class LocationMask {
	private GpsLocation location;
	private double radius;
	
	/**
	 * Stupid and dangerous default constructor to enable subclassing.
	 */
	public LocationMask() {}
	
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param radius in meters
	 */
	public LocationMask(float lat, float lon, double radius) {
		this(new GpsLocation(lat, lon, 0, null), radius);
	}
	
	/**
	 * 
	 * @param location
	 * @param radius in meters
	 */
	public LocationMask(GpsLocation location, double radius) {
		this.location = location;
		this.radius = radius;
	}
	
	public boolean contains(GpsLocation l) {
		return location.getMetersTo(l) <= this.radius;
	}
	
	public GpsLocation getCenter() {
		return this.location;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public GpsTrack mask(GpsTrack track) {
		GpsTrack t = new GpsTrack();
		for (GpsLocation loc : track) {
			if (!contains(loc)) {
				t.add(loc);
			}
		}
		return t;
	}

}
