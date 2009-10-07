package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;

public class MophoEntryListing extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            MophoSelectionForm moForm = (MophoSelectionForm)form;
            int startIndex = moForm.getPage() * moForm.getPageSize();
            int pageSize = moForm.getPageSize();
            int userId = moForm.getUserId();
            Collection entries = new MophoDao().find(moForm);
            
            req.setAttribute("entries", entries);
            req.setAttribute("pageNumber", new Integer(moForm.getPage()));
            req.setAttribute("pageSize", new Integer(pageSize));

            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
}
