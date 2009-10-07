package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;

public class LogEntryListing extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            req.setAttribute("entries", new LogDao().find((SearchForm)form));
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
