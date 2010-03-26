package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;

public class RecentImages extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            int numEntries = Integer.parseInt(req.getParameter("numEntries"));
            Collection recentImages = new AttachmentDao().findRecentImages(numEntries);
            int size = recentImages.size();
            req.setAttribute("recentImages", recentImages);
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
}
