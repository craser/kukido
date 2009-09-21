/*
 * Trackback.java
 *
 * Created on August 26, 2006, 11:05 AM
 */

package net.kukido.blog.datamodel;

import java.util.Date;
import java.net.URL;

/**
 *
 * @author  craser
 */
public class Trackback 
{    
    private int trackbackId;
    private int entryId;
    private boolean isSpam;
    private String direction; // This is either "sent" or "received"
    private Date datePosted;
    private String title;
    private String excerpt;
    private String url;
    private String blogName;
    private String ipAddress;
    private String userAgent;
    private String referrer;
    
    
    public Trackback(String url) {
        setUrl(url);
    }
    
    /** Creates a new instance of Trackback */
    public Trackback() {
    }
    
    /**
     * Getter for property trackbackId.
     * @return Value of property trackbackId.
     */
    public int getTrackbackId() {
        return trackbackId;
    }
    
    /**
     * Setter for property trackbackId.
     * @param trackbackId New value of property trackbackId.
     */
    public void setTrackbackId(int trackbackId) {
        this.trackbackId = trackbackId;
    }
    
    /**
     * Getter for property entryId.
     * @return Value of property entryId.
     */
    public int getEntryId() {
        return entryId;
    }
    
    /**
     * Setter for property entryId.
     * @param entryId New value of property entryId.
     */
    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    
    /**
     * Getter for property isSpam.
     * @return Value of property isSpam.
     */
    public boolean isSpam() {
        return isSpam;
    }
    
    /**
     * Setter for property isSpam.
     * @param isSpam New value of property isSpam.
     */
    public void setIsSpam(boolean isSpam) {
        this.isSpam = isSpam;
    }
    
    /**
     * Getter for property datePosted.
     * @return Value of property datePosted.
     */
    public Date getDatePosted() {
        return datePosted;
    }
    
    /**
     * Setter for property datePosted.
     * @param datePosted New value of property datePosted.
     */
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
    
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /**
     * Getter for property excerpt.
     * @return Value of property excerpt.
     */
    public java.lang.String getExcerpt() {
        return excerpt;
    }
    
    /**
     * Setter for property excerpt.
     * @param excerpt New value of property excerpt.
     */
    public void setExcerpt(java.lang.String excerpt) {
        this.excerpt = excerpt;
    }
    
    /**
     * Getter for property url.
     * @return Value of property url.
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Setter for property url.
     * @param url New value of property url.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Getter for property blogName.
     * @return Value of property blogName.
     */
    public java.lang.String getBlogName() {
        return blogName;
    }
    
    /**
     * Setter for property blogName.
     * @param blogName New value of property blogName.
     */
    public void setBlogName(java.lang.String blogName) {
        this.blogName = blogName;
    }
    
    /**
     * Getter for property ipAddress.
     * @return Value of property ipAddress.
     */
    public java.lang.String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * Setter for property ipAddress.
     * @param ipAddress New value of property ipAddress.
     */
    public void setIpAddress(java.lang.String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    /**
     * Getter for property userAgent.
     * @return Value of property userAgent.
     */
    public java.lang.String getUserAgent() {
        return userAgent;
    }
    
    /**
     * Setter for property userAgent.
     * @param userAgent New value of property userAgent.
     */
    public void setUserAgent(java.lang.String userAgent) {
        this.userAgent = userAgent;
    }
    
    /**
     * Getter for property referrer.
     * @return Value of property referrer.
     */
    public java.lang.String getReferrer() {
        return referrer;
    }
    
    /**
     * Setter for property referrer.
     * @param referrer New value of property referrer.
     */
    public void setReferrer(java.lang.String referrer) {
        this.referrer = referrer;
    }
    
    /**
     * Getter for property direction.
     * @return Value of property direction.
     */
    public java.lang.String getDirection() {
        return direction;
    }
    
    /**
     * Setter for property direction.
     * @param direction Use TrackbackDao.DIRECTION_X values.
     */
    public void setDirection(java.lang.String direction) {
        this.direction = direction;
    }
    
    public boolean equals(Object o)
    {
        return o instanceof Trackback && ((Trackback)o).url.equals(this.url);
    }
    
}
