/*
 * TrackbackUrl.java
 *
 * Created on August 26, 2006, 2:14 PM
 */

package net.kukido.blog.tags;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.net.*;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.datamodel.Comment;

/**
 *
 * @author  craser
 */
public class TrackbackLink extends EntryLink
{
    protected void setHref()
    {
        try
        {
            int entryId = getEntry().getEntryId();
            String page = "trackback/" + entryId;
            setHref(getBaseUrl() + page);
        }
        catch (Exception ignored) {
            ignored.printStackTrace(System.out);
        }
    }
    
}
