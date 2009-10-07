/*
 * GpsBounds.java
 *
 * Created on November 1, 2007, 10:23 PM
 */

package net.kukido.maps;

/**
 *
 * @author  craser
 */
public class GpsBounds 
{
    private float minLatitude;
    private float maxLatitude;
    private float minLongitude;
    private float maxLongitude;
    
    public GpsBounds() {
    
    }
    
    /** Creates a new instance of GpsBounds */
    public GpsBounds(float minLat, float maxLat, float minLon, float maxLon) 
    {
        this.minLatitude = minLat;
        this.maxLatitude = maxLat;
        this.minLongitude = minLon;
        this.maxLongitude = maxLon;
    }
    
    public GpsLocation getCenter()
    {
        float lat = (maxLatitude + minLatitude) / 2;
        float lon = (maxLongitude + minLongitude) / 2;
        GpsLocation location = new GpsLocation(lat, lon, 0f, null);
        return location;
    }
    
    /**
     * Getter for property minLatitude.
     * @return Value of property minLatitude.
     */
    public float getMinLatitude() {
        return minLatitude;
    }    
    
    /**
     * Setter for property minLatitude.
     * @param minLatitude New value of property minLatitude.
     */
    public void setMinLatitude(float minLatitude) {
        this.minLatitude = minLatitude;
    }    
    
    /**
     * Getter for property maxLatitude.
     * @return Value of property maxLatitude.
     */
    public float getMaxLatitude() {
        return maxLatitude;
    }
    
    /**
     * Setter for property maxLatitude.
     * @param maxLatitude New value of property maxLatitude.
     */
    public void setMaxLatitude(float maxLatitude) {
        this.maxLatitude = maxLatitude;
    }
    
    /**
     * Getter for property minLongitude.
     * @return Value of property minLongitude.
     */
    public float getMinLongitude() {
        return minLongitude;
    }
    
    /**
     * Setter for property minLongitude.
     * @param minLongitude New value of property minLongitude.
     */
    public void setMinLongitude(float minLongitude) {
        this.minLongitude = minLongitude;
    }
    
    /**
     * Getter for property maxLongitude.
     * @return Value of property maxLongitude.
     */
    public float getMaxLongitude() {
        return maxLongitude;
    }
    
    /**
     * Setter for property maxLongitude.
     * @param maxLongitude New value of property maxLongitude.
     */
    public void setMaxLongitude(float maxLongitude) {
        this.maxLongitude = maxLongitude;
    }
    
}
