package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.Tag;
import net.kukido.blog.forms.*;

public class RecentTags extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            int numTags = Integer.parseInt(req.getParameter("numTags"));
            Collection c = new TagLinkDao().findRecentTags(numTags);            
            List tags = new ArrayList(c);
            Comparator alphabetical = new Comparator() {
                public int compare(Object o1, Object o2) {
                    Tag a = (Tag)o1;
                    Tag b = (Tag)o2;
                    return a.getName().compareTo(b.getName());                    
                }
            };
            Collections.sort(tags, alphabetical);
            req.setAttribute("tags", tags);
            req.setAttribute("title", req.getParameter("title"));
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
}