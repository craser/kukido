package net.kukido.blog.datamodel;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;

public class Location {
	private GpsLocation location;
	private double radius;
	
	/**
	 * Stupid and dangerous default constructor to enable subclassing.
	 */
	public Location() {}
	
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @param radius in meters
	 */
	public Location(float lat, float lon, double radius) {
		this(new GpsLocation(lat, lon, 0, null), radius);
	}
	
	/**
	 * 
	 * @param location
	 * @param radius in meters
	 */
	public Location(GpsLocation location, double radius) {
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

}
