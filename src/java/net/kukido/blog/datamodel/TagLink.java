/*
 * TagLink.java
 *
 * Created on August 7, 2005, 12:16 PM
 */

package net.kukido.blog.datamodel;

import java.util.Date;

/**
 *
 * @author  craser
 */
public class TagLink 
{
    private int objectId;
    private Tag tag;
    private Date dateTagged;
    
    /** Creates a new instance of TagLink */
    public TagLink() 
    {
    }
    
    public void setObjectId(int objectId)
    {
        this.objectId = objectId;
    }
    
    public int getObjectId()
    {
        return objectId;
    }
    
    public void setTag(Tag tag)
    {
        this.tag = tag;
    }
    
    public Tag getTag()
    {
        return tag;
    }
    
    public Date getDateTagged()
    {
        return dateTagged;
    }
    
    public void setDateTagged(Date dateTagged)
    {
        this.dateTagged = dateTagged;
    }
}
