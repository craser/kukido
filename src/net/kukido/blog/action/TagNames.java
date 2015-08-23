package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import net.kukido.blog.dataaccess.*;

public class TagNames extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            req.setAttribute("tags", new TagDao().findAllTags());
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
