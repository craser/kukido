/*
 * LogEntry.java
 *
 * Created on September 28, 2003, 9:04 PM
 */

package net.kukido.blog.datamodel;

import java.util.*;

/**
 * Represents a single log entry.
 *
 * @author  craser
 */
public class LogEntry extends LogEntryHeader
{
    private Collection<Attachment> attachments;
    private Collection<Comment> comments;

    /**
     * @return a list of valid values for displayClass property.
     */
    static public Collection<String> getDisplayClassOptions() {
        List<String> options = new ArrayList<String>(2);
        options.add("thumbnail");
        options.add("postcard");
        options.add("map");
        return options;
    }
    
    /**
     * Creates a new instance of LogEntry
     * */
    public LogEntry()
    {
	this.attachments = new ArrayList<Attachment>();
        this.comments = new ArrayList<Comment>();
    }
    
    public LogEntry(LogEntryHeader header)
    {
        super(header);
        this.attachments = new ArrayList<Attachment>();
        this.comments = new ArrayList<Comment>();
    }

    public void setAttachments(Collection<Attachment> attachments)
    {
	this.attachments = attachments;
    }

    public Collection<Attachment> getAttachments()
    {
	return attachments;
    }

    public void addAttachment(Attachment a)
    {
	attachments.add(a);
    }

    public void removeAttachment(Attachment a)
    {
	attachments.remove(a);
    }
    
    public void addTag(Tag tag)
    {
        Collection<Tag> tags = getTags();
        tags.add(tag);
        setTags(tags);
    }
    
    public void removeTag(Tag tag)
    {
        Collection<Tag> tags = getTags();
        tags.remove(tag);
        setTags(tags);
    }

    public boolean getHasGalleryImages()
    {
        return hasAttachmentType("image");
    }
    
    public boolean getHasMaps()
    {
        return hasAttachmentType("map");
    }
    
    public boolean getHasDocuments()
    {
        return hasAttachmentType("document");
    }
    
    public boolean hasAttachmentType(String type)
    {
        for (Iterator<Attachment> as = attachments.iterator(); as.hasNext(); )
        {
            if (type.equals(((Attachment)as.next()).getFileType()))
                return true;
        }
        return false;
    }

    public boolean getHasAttachments()
    {
        for (Iterator<Attachment> as = attachments.iterator(); as.hasNext(); )
        {
            if (!((Attachment)as.next()).getIsGalleryImage())
                return true;
        }
        return false;
    }
    
    public void setComments(Collection<Comment> comments)
    {
        this.comments = comments;
    }
    
    public Collection<Comment> getComments()
    {
        return comments;
    }
    
    public int getNumComments()
    {
        return (comments == null) ? 0 : comments.size();
    }
}
