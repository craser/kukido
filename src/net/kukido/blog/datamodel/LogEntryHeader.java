/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.datamodel;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author craser
 */
public class LogEntryHeader implements Serializable
{
    private int entryId;
    private java.util.Date datePosted;
    private java.util.Date lastUpdated;
    private int userId;
    private String userName;
    private boolean allowComments;
    private String title;
    private String imageFileName;
    private String imageFileType;
    private String intro;
    private String body;
    private String viaTitle;
    private String viaText;
    private String viaUrl;
    
    
    // Summary info for detail-level items
    private int numTrackbacks;
    private int numComments;
    private boolean hasGalleryImages;
    private boolean hasAttachments;
    
    private Collection tags;
    
    public LogEntryHeader() 
    {
        this.tags = new ArrayList();
    }
    
    /**
     * Copy constructor
     * @param h The LogEntryHeader to copy.
     */
    public LogEntryHeader(LogEntryHeader h)
    {
        this.entryId = h.entryId;
        this.datePosted = h.datePosted;
        this.lastUpdated = h.lastUpdated;
        this.userId = h.userId;
        this.userName = h.userName;
        this.allowComments = h.allowComments;
        this.title = h.title;
        this.imageFileName = h.imageFileName;
        this.imageFileType = h.imageFileType;
        this.intro = h.intro;
        this.body = h.body;
        this.viaTitle = h.viaTitle;
        this.viaText = h.viaText;
        this.viaUrl = h.viaUrl;
        this.numTrackbacks = h.numTrackbacks;
        this.numComments = h.numComments;
        this.hasGalleryImages = h.hasGalleryImages;
        this.hasAttachments = h.hasAttachments;
    }
    

    public void setEntryId(int entryId)
    {
	this.entryId = entryId;
    }

    public int getEntryId()
    {
	return entryId;
    }

    public void setUserId(int userId)
    {
	this.userId = userId;
    }

    public int getUserId()
    {
	return userId;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setAllowComments(boolean allowComments)
    {
        this.allowComments = allowComments;
    }
    
    public boolean getAllowComments()
    {
        return allowComments;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getTitle()
    {
	return title;
    }
    
    public void setImageFileName(String imageFileName)
    {
        this.imageFileName = ("".equals(imageFileName)) ? null : imageFileName;
    }
    
    public String getImageFileName()
    {
        return imageFileName;
    }
    
    public void setImageFileType(String imageFileType)
    {
        this.imageFileType = imageFileType;
    }
    
    public String getImageFileType()
    {
        return imageFileType;
    }
    
    public void setIntro(String intro)
    {
        this.intro = intro;
    }
    
    public String getIntro()
    {
        return intro;
    }

    public void setBody(String body)
    {
	this.body = body;
    }

    public String getBody()
    {
	return body;
    }
    
    public void setViaTitle(String viaTitle)
    {
	this.viaTitle = viaTitle;
    }

    public String getViaTitle()
    {
	return viaTitle;
    }

    public void setViaText(String viaText)
    {
        this.viaText = viaText;
    }
    
    public String getViaText()
    {
        return viaText;
    }
    
    public void setViaUrl(String viaUrl)
    {
        this.viaUrl = viaUrl;
    }
    
    public String getViaUrl()
    {
        return viaUrl;
    }

    public void setDatePosted(java.util.Date datePosted)
    {
	this.datePosted = datePosted;
    }

    public java.util.Date getDatePosted()
    {
	return datePosted;
    }

    public void setLastUpdated(Date lastUpdated)
    {
	this.lastUpdated = lastUpdated;
    }

    public Date getLastUpdated()
    {
	return lastUpdated;
    }
    
    public void setNumComments(int numComments)
    {
        this.numComments = numComments;
    }
    
    public int getNumComments()
    {
        return numComments;
    }
    
    public void setNumTrackbacks(int numTrackbacks)
    {
        this.numTrackbacks = numTrackbacks;
    }
    
    public int getNumTrackbacks()
    {
        return numTrackbacks;
    }
    
    public void setTags(Collection tags)
    {
        this.tags = tags;
    }
    
    public Collection getTags()
    {
        return tags;
    }

}
