/*
 * BatchGeotagForm.java
 *
 * Created on October 29, 2007, 7:40 PM
 */

package net.kukido.blog.forms;

import net.kukido.blog.datamodel.Comment;
import net.kukido.blog.servlet.CommentCookie;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;

/**
 *
 * @author  craser
 */
public class BatchGeotagForm extends ActionForm
{
    int entryId;
    int mapId;
    int timeOffset; // Added to camera EXIF date to get actual time.
    boolean replaceExistingTags;
    
    /** Creates a new instance of BatchGeotagForm */
    public BatchGeotagForm() {
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
     * Getter for property timeOffset.
     * @return Value of property timeOffset.
     */
    public int getTimeOffset() {
        return timeOffset;
    }
    
    /**
     * Setter for property timeOffset.
     * @param timeOffset New value of property timeOffset.
     */
    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }
    
    /**
     * Getter for property replaceExistingTags.
     * @return Value of property replaceExistingTags.
     */
    public boolean getReplaceExistingTags() {
        return replaceExistingTags;
    }
    
    /**
     * Setter for property replaceExistingTags.
     * @param replaceExistingTags New value of property replaceExistingTags.
     */
    public void setReplaceExistingTags(boolean replaceExistingTags) {
        this.replaceExistingTags = replaceExistingTags;
    }
    
}
