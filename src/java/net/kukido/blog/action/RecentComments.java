package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.util.BucketMap;

public class RecentComments extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            // First, grab the recent comments, and put them into lists
            // keyed by the Entry ID.
            int numComments = Integer.parseInt(req.getParameter("numComments"));
            Collection comments = new CommentDao().findRecentComments(numComments);
            Map commentsById = new BucketMap();
            for (Iterator i = comments.iterator(); i.hasNext();) {
                Comment c = (Comment)i.next();
                Integer id = new Integer(c.getEntryId());
                commentsById.put(id, c);
            }
            
            // Grab the entries associated with these comments.
            Collection entries = new LogDao().findByEntryIds(commentsById.keySet());
            // Put those entries in a Map so we can find them by ID later.
            Map entryById = new HashMap();
            for (Iterator i = entries.iterator(); i.hasNext();) {
                LogEntry e = (LogEntry)i.next();
                Integer id = new Integer(e.getEntryId());
                entryById.put(id, e);
            }
            
            // Now, sort all the entries in reverse chronological order by 
            // most recent comment.
            List orderedEntries = new ArrayList(entries.size()); // Sorted entries that we'll later pass to the JSP.
            Set entryIds = new HashSet(); // Keep track of processed entries.
            for (Iterator i = comments.iterator(); i.hasNext();) {
                Comment c = (Comment)i.next();
                Integer id = new Integer(c.getEntryId());
                if (entryIds.contains(id)) continue; // Process each entry once.
                List cs = (List)commentsById.get(id);
                LogEntry e = (LogEntry)entryById.get(id);
                e.setComments(cs); // Overwrite this entry's comments so we only show the recent ones.
                orderedEntries.add(e);
                entryIds.add(id);
            }
            
            req.setAttribute("entries", orderedEntries); // Entries sorted by comment
            req.setAttribute("comments", comments); // List of comments in reverse chronological order.
            
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
}
