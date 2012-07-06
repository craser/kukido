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
    private Collection attachments;
    private Collection comments;
    private List trackbacks; // Watch out!  Used both for sent and received Trackbacks!
    

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
	this.attachments = new ArrayList();
        this.comments = new ArrayList();
        this.trackbacks = new ArrayList();
        
    }
    
    public LogEntry(LogEntryHeader header)
    {
        super(header);
        this.attachments = new ArrayList();
        this.comments = new ArrayList();
        this.trackbacks = new ArrayList();
    }

    public void setAttachments(Collection attachments)
    {
	this.attachments = attachments;
    }

    public Collection getAttachments()
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
        Collection tags = getTags();
        tags.add(tag);
        setTags(tags);
    }
    
    public void removeTag(Tag tag)
    {
        Collection tags = getTags();
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
        for (Iterator as = attachments.iterator(); as.hasNext(); )
        {
            if (type.equals(((Attachment)as.next()).getFileType()))
                return true;
        }
        return false;
    }

    public boolean getHasAttachments()
    {
        for (Iterator as = attachments.iterator(); as.hasNext(); )
        {
            if (!((Attachment)as.next()).getIsGalleryImage())
                return true;
        }
        return false;
    }
    
    public void setComments(Collection comments)
    {
        this.comments = comments;
    }
    
    public Collection getComments()
    {
        return comments;
    }
    
    public int getNumComments()
    {
        return (comments == null) ? 0 : comments.size();
    }
    
    public void setTrackbacks(List trackbacks)
    {
        if (trackbacks == null) { 
            this.trackbacks.clear(); 
        }
        this.trackbacks = trackbacks;
    }
    
    public List getTrackbacks()
    {
        return trackbacks;
    }
    
    public int getNumTrackbacks()
    {
        return (trackbacks == null) ? 0 : trackbacks.size();
    }
}
