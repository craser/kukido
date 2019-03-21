package net.kukido.blog.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;

public class SearchComments extends Action 
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException
    {
        try
        {
            CommentSelectionForm commentForm = (CommentSelectionForm)form;
            req.setAttribute("comments", new CommentDao().find(commentForm));
            if (commentForm.getEntryId() >= 0)
                req.setAttribute("entry", new LogDao().findByEntryId(commentForm.getEntryId()));
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
