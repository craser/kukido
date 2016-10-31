package net.kukido.blog.datamodel;

import java.io.*;
import java.util.*;

public class Attachment implements Serializable
{
    static public final String TYPE_IMAGE = "image";
    static public final String TYPE_MAP = "map";
    static public final String TYPE_DOCUMENT = "document";
    static public final String TYPE_BACKUP = "backup";
    
    private int attachmentId;
    private int entryId;
    private boolean isGalleryImage;
    private String activityId;
    private String fileName;
    private String mimeType;
    private String fileType;
    private int userId;
    private String userName;
    private Date datePosted;
    private Date dateTaken;
    private String title;
    private String description;
    private Collection<Geotag> geotags;
    private byte[] bytes;
    
   
    /**
     * Create a new list each time to avoid ill-bahaved code from monkeying
     * with the list.  Values returned in the list should be identical to
     * valid values for the ATTACHMENT.File_Type field in the database.
     *
     * @return The available fileType options for all attachments.
     */
    static public Collection<String> getFileTypeOptions() {
        List<String> options = new ArrayList<String>(3);
        options.add(TYPE_IMAGE);
        options.add(TYPE_MAP);
        options.add(TYPE_DOCUMENT);
        return options;
    }
    
    public Attachment copy() {
    	Attachment copy = new Attachment();
    	copy.entryId = this.entryId;
    	copy.isGalleryImage = this.isGalleryImage;
        copy.activityId = this.activityId;
    	copy.fileName = this.fileName;
    	copy.mimeType = this.mimeType;
    	copy.fileType = this.fileType;
    	copy.userId = this.userId;
    	copy.userName = this.userName;
    	copy.datePosted = this.datePosted;
    	copy.dateTaken = this.dateTaken;
    	copy.title = this.title;
    	copy.description = this.description;
    	copy.geotags = new ArrayList<Geotag>(this.geotags);
    	if (this.bytes != null) {
    		copy.bytes = Arrays.copyOf(this.bytes, this.bytes.length);
    	}
    	
    	return copy;
    }
    
    public void setAttachmentId(int attachmentId)
    {
	this.attachmentId = attachmentId;
    }

    public int getAttachmentId()
    {
	return attachmentId;
    }

    public void setEntryId(int entryId)
    {
	this.entryId = entryId;
    }

    public int getEntryId()
    {
	return entryId;
    }

    public void setIsGalleryImage(boolean isGalleryImage)
    {
	this.isGalleryImage = isGalleryImage;
    }

    public boolean getIsGalleryImage()
    {
	return isGalleryImage;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityId() {
        return activityId;
    }
    
    public boolean getIsMap()
    {
        return TYPE_MAP.equals(getFileType());
    }

    public void setFileName(String fileName)
    {
	this.fileName = fileName;
    }

    public String getFileName()
    {
	return fileName;
    }

    public void setMimeType(String mimeType)
    {
	this.mimeType = mimeType;
    }

    public String getMimeType()
    {
	return mimeType;
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

    public void setDatePosted(Date datePosted)
    {
	this.datePosted = datePosted;
    }

    public Date getDatePosted()
    {
	return datePosted;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getTitle()
    {
	return title;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public String getDescription()
    {
	return description;
    }

    public void setBytes(byte[] bytes)
    {
	this.bytes = bytes;
    }

    public byte[] getBytes()
    {
	return bytes;
    }
    
    /**
     * Getter for property dateTaken.
     * @return Value of property dateTaken.
     */
    public java.util.Date getDateTaken() {
        return dateTaken;
    }
    
    /**
     * Setter for property dateTaken.
     * @param dateTaken New value of property dateTaken.
     */
    public void setDateTaken(java.util.Date dateTaken) {
        this.dateTaken = dateTaken;
    }
    
    /**
     * Getter for property geotags.
     * @return Value of property geotags.
     */
    public Collection<Geotag> getGeotags() {
        return geotags;
    }
    
    /**
     * Setter for property geotags.
     * @param geotags New value of property geotags.
     */
    public void setGeotags(Collection<Geotag> geotags) {
        this.geotags = geotags;
    }
    
    /**
     * Getter for property fileType.
     * @return Either "document", "map", or "image"
     */
    public String getFileType() {
        return fileType;
    }
    
    /**
     * Given string must be either "document", "map", or "image".
     * Any value not part of the enumerated values for the
     * ATTACHMENTS.File_Type field will cause problems when you 
     * try to save this object to the database.
     *
     * @param fileType Either "document", "map", or "image"
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
}
