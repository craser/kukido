/*
 * Tag.java
 *
 * Created on August 7, 2005, 11:32 AM
 */

package net.kukido.blog.datamodel;

/**
 *
 * @author  craser
 */
public class Tag implements java.io.Serializable
{
    private int tagId;
    private String name;
    
    public Tag()
    {
    }
    
    public Tag(String name)
    {
        setName(name);
    }
   
    public Tag(int tagId, String name) 
    {
        setTagId(tagId);
        setName(name);
    }
    
    public void setTagId(int tagId)
    {
        this.tagId = tagId;
    }
    
    public int getTagId()
    {
        return tagId;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String toString()
    {
        return name;
    }
    
    public boolean equals(Object o)
    {
        return (o instanceof Tag) && ((Tag)o).tagId == this.tagId;
    }
    
    public int hashCode()
    {
        return this.name.hashCode();
    }    
}
