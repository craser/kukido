/*
 * MophoEntry.java
 *
 * Created on March 26, 2004, 8:42 PM
 */

package net.kukido.blog.datamodel;

import java.util.Date;

/**
 *
 * @author  craser
 */
public class MophoEntry implements java.io.Serializable
{
    private int entryId;
    private Date datePosted;
    private int userId;
    private String userName;
    private String title;
    private String fileName;
    private byte[] bytes;
    
    /** Creates a new instance of MophoEntry */
    public MophoEntry()
    {
    }

    public void setEntryId(int entryId)
    {
	this.entryId = entryId;
    }

    public int getEntryId()
    {
	return entryId;
    }

    public void setDatePosted(Date datePosted)
    {
	this.datePosted = datePosted;
    }

    public Date getDatePosted()
    {
	return datePosted;
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

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getTitle()
    {
	return title;
    }

    public void setFileName(String fileName)
    {
	this.fileName = fileName;
    }

    public String getFileName()
    {
	return fileName;
    }

    public void setBytes(byte[] bytes)
    {
	this.bytes = bytes;
    }

    public byte[] getBytes()
    {
	return bytes;
    }
}
