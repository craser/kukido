/*
 * GpsTrack.java
 *
 * Created on October 24, 2007, 9:49 PM
 */

package net.kukido.maps;

import java.util.*;
import java.text.DecimalFormat;

/**
 * Represents a series of Lat/Lon/Elevation points in chronological order.
 * @author  craser
 */
public class GpsTrack extends ArrayList<GpsLocation>
{
    static private final long DAY = 1000l * 60l * 60l * 24l;
    static private final long HOUR = 1000l * 60l * 60l;
    static private final long MINUTE = 1000l * 60l;
    static private final long SECOND = 1000l;
    
    static private final double MILES_PER_KM = 0.621371192d;
    
    private String name;
    
    /**
     * This is a total hack, but I'm sick and I just want this done.  When 
     * I think of something better, I'll fix this.
     *
     * @return a Date object with appropriate properties set to represent a duration 
     * in days, hours, minutes, and seconds.
     */
    public long getDuration()
    {
        if (size() > 0) {
            long start = ((GpsLocation)get(0)).getTimestamp().getTime();
            long finish = ((GpsLocation)get(size() -1)).getTimestamp().getTime();
            long d = Math.abs(finish - start);
            
            return d;
        }
        else {
            return 0l;
        }
    }
    
    public Date getStartTime()
    {
        if (size() < 1) { throw new IllegalStateException("Unable to determine start of route: no data."); }
        return ((GpsLocation)get(0)).getTimestamp();
    }
    
    public Date getFinishTime()
    {
        if (size() < 1) { throw new IllegalStateException("Unable to determine finish of route: no data."); }
        return ((GpsLocation)get(size() - 1)).getTimestamp();
    }
    
    public String getFormattedDuration()
    {
        if (size() <= 0) { return "00:00:00"; }
        
        long start = getStartTime().getTime();
        long finish = getFinishTime().getTime();
        
        long d = finish - start;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        
        while (d > DAY) { days++; d -= DAY; }
        while (d > HOUR) { hours++; d -= HOUR; }
        while (d > MINUTE) { minutes++; d -= MINUTE; }
        while (d > SECOND) { seconds++; d -= SECOND; }
       
        DecimalFormat dd = new DecimalFormat("00");
        StringBuffer b = new StringBuffer();
        if (days > 0) { b.append(days).append(":"); }
        b.append(dd.format(hours)).append(":");
        b.append(dd.format(minutes)).append(":");
        b.append(dd.format(seconds));
        
        return b.toString();
    }
    
    public float getClimbingVertical()
    {
        float total = 0f;
        float last = 0f;
        Iterator i = iterator();
        if (i.hasNext())
        {
            last = ((GpsLocation)i.next()).getElevation();
            while (i.hasNext())
            {
                GpsLocation l = (GpsLocation)i.next();
                float ele = l.getElevation();
                if (ele > last) {
                    total += (ele - last);
                }
                last = ele;
            }
        }
        
        return total;
    }
    /**
     * @return a GpsLocation with only the latitude and longitude set.
     */
    public GpsLocation getCenter()
    {
        GpsBounds b = getBounds();
        float lat = (b.getMinLatitude() + b.getMaxLatitude()) / 2f;
        float lon = (b.getMinLongitude() + b.getMaxLongitude()) / 2f;
        
        GpsLocation l = new GpsLocation();
        l.setLatitude(lat);
        l.setLongitude(lon);
        
        return l;
    }
    
    public GpsBounds getBounds()
    {
        GpsLocation start = (GpsLocation)get(0);
        float minLat = start.getLatitude();
        float maxLat = start.getLatitude();
        float minLon = start.getLongitude();
        float maxLon = start.getLongitude();
        
        for (Iterator i = iterator(); i.hasNext(); )
        {
            GpsLocation l = (GpsLocation)i.next();
            minLat = Math.min(minLat, l.getLatitude());
            maxLat = Math.max(maxLat, l.getLatitude());
            minLon = Math.min(minLon, l.getLongitude());
            maxLon = Math.max(maxLon, l.getLongitude());
        }
        
        GpsBounds bounds = new GpsBounds(minLat, maxLat, minLon, maxLon);
        return bounds;
    }
    
    /**
     * @return the closest location on this track.
     */
    public GpsLocation getLocationByTimestamp(Date timestamp)
    {
        System.out.println("Looking for location at time: " + timestamp.toString());
        GpsLocation first = (GpsLocation)get(0);
        GpsLocation last = (GpsLocation)get(size() - 1);
        System.out.println("Looking between " + first.getTimestamp().toString() + " and " + last.getTimestamp().toString());

        GpsLocation closest = (GpsLocation)get(0);
        for (int i = 1; i < size(); i++) { // Code now, optimize later.
            GpsLocation loc = (GpsLocation)get(i);
            if (loc.getTimestamp().after(timestamp)) {
                break; // Not as good as "closest", but this handles TIME ZONES (I hope?)
            }
            else {
                closest = loc;
            }
        }
        
        System.out.println("Closest timestamp found: " + closest.getTimestamp().toString());        
        return closest;
    }
    
    /**
     * Computes the total distance in km covered by this track.
     */
    public double getKilometers()
    {
        double km = 0d; // This is actually in meters until just before we return.
        Iterator i = iterator();
        GpsLocation a = (GpsLocation)i.next();
        
        while (i.hasNext()) {
            GpsLocation b = (GpsLocation)i.next();
            //km += a.getMetersTo(b); // Note that "km" is actually in meters until just before we return.
            km += a.getMetersToElevation(b); // Note that "km" is actually in meters until just before we return.
            a = b;
        }
        
        km /= 1000d; // Divide by 1000 to convert meters to km.
        return km;
    }
    
    public double getMiles()
    {
        double km = getKilometers();
        double miles = km * MILES_PER_KM;
        return miles;
    }
    
    public GpsTrack getThinnedTrack() {
        return getThinnedTrack(500);
    }
    
    public GpsTrack getThinnedTrack(int maxNodes)
    {
        if (size() < maxNodes) {
            return this;
        }
        SortedSet<Integer> included = new TreeSet<Integer>();
        included.add(0);
        included.add(size() - 1);
        
        while (included.size() < maxNodes) {
            addMostInteresting(this, included);
        }
        
        GpsTrack thin = new GpsTrack();
        for (int i : included) {
            thin.add(get(i));
        }
        
        return thin;
    }
    
    private void addMostInteresting(GpsTrack track, SortedSet<Integer> included)
    {
        GpsLocation a = null;
        GpsLocation b = null;
        double bearing = 0;
        double score = 0;
        int include = -1;
        for (int i = 0; i < track.size(); i++) {
            if ((i + 1) >= track.size()) { // Last node included always in calling method.
                break;
            }
            if (included.contains(i)) {
                a = track.get(i);
                int j = included.tailSet(i+1).first();
                b = track.get(j);
                bearing = a.getBearingTo(b);
                continue;
            }
            
            GpsLocation l = track.get(i);
            double distance = a.getMetersTo(l);
            double theta = bearing - a.getBearingTo(l);
            theta = Math.abs(theta % Math.PI);
            double offset = Math.abs(distance * Math.sin(theta));
            if (offset > score) {
                score = offset;
                include = i;
            }
        }
        
        included.add(include);
    }

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
}
