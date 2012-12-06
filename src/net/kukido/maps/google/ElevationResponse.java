package net.kukido.maps.google;

import java.util.ArrayList;
import java.util.List;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;

/**
 * Represents a response from Google's elevation API.
 * @see https://developers.google.com/maps/documentation/elevation
 */
public class ElevationResponse
{
	private String status;
	private List<GpsLocation> locs;

	public ElevationResponse()
	{
		this("OK", new ArrayList<GpsLocation>());
	}

	public ElevationResponse(String status, List<GpsLocation> locs)
	{
		this.status = status;
		this.locs = locs;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void add(GpsLocation loc)
	{
		locs.add(loc);
	}

	public List<GpsLocation> getLocations()
	{
		// Copy the list, so that calling classes can't mess with our data.
		return new ArrayList<GpsLocation>(this.locs);
	}

	/**
	 * Assigns new elevations to the locations in the given GpsTrack
	 * based on the information provided by Google.
	 */
	public GpsTrack fixElevations(GpsTrack track)
	{
		for (GpsLocation loc : track) {
			for (GpsLocation e : locs) {
				if (haveSameLocation(loc, e)) {
					loc.setElevation(e.getElevation());
					break;
				}
			}
			// Signal a warning if we don't find a match before
			// running off the end of the list.
			System.out.println("WARNING: No match found for location " + format(loc));
		}

		return track;
	}

	private String format(GpsLocation loc) 
	{
		return "GpsLocation[" + loc.getLatitude() + ", " + loc.getLongitude() + "]";
	}

	/**
	 * Might need to tweak this to deal with float-related
	 * irregularities, vagaries of Google's response, etc.
	 */
	private boolean haveSameLocation(GpsLocation a, GpsLocation b) 
	{
		return a.getLatitude() == b.getLatitude()
			&& a.getLongitude() == b.getLongitude();
	}
}
		