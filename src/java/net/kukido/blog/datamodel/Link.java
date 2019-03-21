package net.kukido.blog.datamodel;

import java.util.*;
import java.io.Serializable;

/**
 * Represents a single "recent link" from the LINKS table in the
 * database.
 **/
public class Link implements Serializable
{
    private int linkId;
    private java.util.Date datePosted;
    private String title;
    private String text;
    private String url;


    public void setLinkId(int linkId)
    {
	this.linkId = linkId;
    }

    public int getLinkId()
    {
	return linkId;
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

    public void setText(String text)
    {
	this.text = text;
    }

    public String getText()
    {
	return text;
    }

    public void setUrl(String url)
    {
	this.url = url;
    }

    public String getUrl()
    {
	return url;
    }

}
