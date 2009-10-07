package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;

public class Search extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            SearchForm searchForm = (SearchForm)form;
            Collection entries = new LogDao().find(searchForm);
            
            // Collect all the tags that either match the search term,
            // or are attached to the log entries above
            Set tagSet = new HashSet();
            //tags.addAll(new TagDao().find(searchForm));
            for (Iterator i = entries.iterator(); i.hasNext(); ) {
                LogEntryHeader entry = (LogEntryHeader)i.next();
                tagSet.addAll(entry.getTags());
            }
            
            // Put the collected tags in a list and sort alphabetically.
            List tags = new ArrayList(tagSet);
            Comparator alphabetical = new Comparator() {
                public int compare(Object o1, Object o2) {
                    Tag a = (Tag)o1;
                    Tag b = (Tag)o2;
                    return a.getName().compareTo(b.getName());                    
                }
            };
            Collections.sort(tags, alphabetical);            
            
            // Stuff the tags and the entries in the request, and 
            // punt to the JSP
            req.setAttribute("tags", tags);
            req.setAttribute("entries", entries);
            ActionForward fw = mapping.findForward("success");
            return fw;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
}
