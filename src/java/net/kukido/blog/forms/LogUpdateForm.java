/*
 * LogUpdateForm.java
 *
 * Created on February 23, 2004, 10:04 AM
 */

package net.kukido.blog.forms;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;

/**
 *
 * @author  craser
 */
public class LogUpdateForm extends ActionForm
{
    private LogEntry entry;
    
    public LogUpdateForm()
    {
	setEntry(new LogEntry());
    }
    
    public void setTags(String tagList)
        throws DataAccessException
    {
        StringTokenizer tokens = new StringTokenizer(tagList, " ");
        Collection tags = new ArrayList();
        while (tokens.hasMoreTokens())
        {
            String tagName = (String)tokens.nextToken();
            tags.add(new Tag(tagName));
        }
        
        tags = new TagDao().create(tags);
        entry.setTags(tags);
    }
    
    public String getTags()
    {
        Collection tags = entry.getTags() == null
            ? new ArrayList()
            : entry.getTags();
        StringBuffer tagList = new StringBuffer();
        for (Iterator i = tags.iterator(); i.hasNext(); )
        {
            Tag tag = (Tag)i.next();
            tagList.append(tag.getName());
            if (i.hasNext()) tagList.append(" ");
        }
        
        return tagList.toString();
    }

    public void setEntry(LogEntry entry)
    {
	this.entry = entry;
    }

    public LogEntry getEntry()
    {
	return entry;
    }
        
    public Collection getDisplayClassOptions() {
        return LogEntry.getDisplayClassOptions();
    }
    
    public Collection getAttachmentFileTypeOptions() {
        return Attachment.getFileTypeOptions();
    }
    
    /**
     * Ensures that we have values for both the title and intro fields.  Does
     * no checking for presence or validity of the body field.
     **/
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
	ActionErrors errors = new ActionErrors();
	if ("".equals(entry.getTitle()) || null == entry.getTitle())
	    errors.add("title", new ActionMessage("error.missing.title"));
	if ("".equals(entry.getIntro()) || null == entry.getIntro())
	    errors.add("intro", new ActionMessage("error.missing.intro"));
	return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        //setEntry(new LogEntry());
    }
}
