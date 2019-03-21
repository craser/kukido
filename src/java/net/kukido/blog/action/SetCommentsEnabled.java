/*
 * LogUpdate.java
 *
 * Created on October 12, 2003, 4:52 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.blog.forms.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  craser
 * @version
 */
public class SetCommentsEnabled extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException
    {
        try
        {
            boolean enabled = Boolean.parseBoolean(req.getParameter("enabled"));
            new LogDao().enableComments(enabled);
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }

}