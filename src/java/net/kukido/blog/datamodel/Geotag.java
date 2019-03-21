/*
 * Geotag.java
 *
 * Created on October 23, 2007, 7:43 PM
 */

package net.kukido.blog.datamodel;

import java.util.Date;
import net.kukido.maps.*;

/**
 * This class links an attachment (a file stored in the ATTACHMENTS table)
 * with a particular geographic location, expressed as latitude, longitude,
 * and elevation.
 *
 * @author  craser
 */
public class Geotag implements java.io.Serializable
{
    private int tagId;
    private int mapId; // Attachment_ID for GPX map used to generate this tag, if appropriate.
    private int attachmentId; // Attachment this tag locates.
    private Date dateTagged;
    private Date timestamp;
    private float latitude;
    private float longitude;
    private float elevation;
    
    /** Creates a new instance of Geotag */
    public Geotag() {
    }
    
    public Geotag(GpsLocation loc)
    {
        this.dateTagged = new Date();
        this.latitude = loc.getLatitude();
        this.longitude = loc.getLongitude();
        this.elevation = loc.getElevation();
        this.timestamp = loc.getTimestamp();
    }
    
    /**
     * Getter for property tagId.
     * @return Value of property tagId.
     */
    public int getTagId() {
        return tagId;
    }
    
    /**
     * Setter for property tagId.
     * @param tagId New value of property tagId.
     */
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
    
    /**
     * Getter for property mapId.
     * @return Value of property mapId.
     */
    public int getMapId() {
        return mapId;
    }
    
    /**
     * Setter for property mapId.
     * @param mapId New value of property mapId.
     */
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    
    /**
     * Getter for property attachmentId.
     * @return Value of property attachmentId.
     */
    public int getAttachmentId() {
        return attachmentId;
    }
    
    /**
     * Setter for property attachmentId.
     * @param attachmentId New value of property attachmentId.
     */
    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    /**
     * Getter for property dateTagged.
     * @return Value of property dateTagged.
     */
    public java.util.Date getDateTagged() {
        return dateTagged;
    }
    
    /**
     * Setter for property dateTagged.
     * @param dateTagged New value of property dateTagged.
     */
    public void setDateTagged(java.util.Date dateTagged) {
        this.dateTagged = dateTagged;
    }
    
    /**
     * Getter for property latitude.
     * @return Value of property latitude.
     */
    public float getLatitude() {
        return latitude;
    }
    
    /**
     * Setter for property latitude.
     * @param latitude New value of property latitude.
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    
    /**
     * Getter for property longitude.
     * @return Value of property longitude.
     */
    public float getLongitude() {
        return longitude;
    }
    
    /**
     * Setter for property longitude.
     * @param longitude New value of property longitude.
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    
    /**
     * Getter for property elevation.
     * @return Value of property elevation.
     */
    public float getElevation() {
        return elevation;
    }
    
    /**
     * Setter for property elevation.
     * @param elevation New value of property elevation.
     */
    public void setElevation(float elevation) {
        this.elevation = elevation;
    }
    
    /**
     * Getter for property timestamp.
     * @return Value of property timestamp.
     */
    public java.util.Date getTimestamp() {
        return timestamp;
    }
    
    /**
     * Setter for property timestamp.
     * @param timestamp New value of property timestamp.
     */
    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }    
}
