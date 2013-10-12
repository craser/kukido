package net.kukido.maps;

public class LocationMask {
	private GpsLocation location;
	private double radius;
	
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

}
