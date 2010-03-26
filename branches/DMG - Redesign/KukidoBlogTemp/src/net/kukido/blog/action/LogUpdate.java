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
public class LogUpdate extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        try
        {
            net.kukido.blog.forms.LogUpdateForm updateForm = (net.kukido.blog.forms.LogUpdateForm)form;            
            User user = (User)req.getSession().getAttribute("user");
            LogEntry entry = updateForm.getEntry();
            entry.setUserId(user.getUserId());
            entry.setUserName(user.getUserName());
            new LogDao().create(entry);
            
            return mapping.findForward("success");
        }
        catch (Exception e)
        {
	    e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
}
