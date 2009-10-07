/*
 * CreateUser.java
 *
 * Created on October 8, 2003, 10:18 PM
 */

package net.kukido.blog.action;

import net.kukido.blog.dataaccess.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.security.*;
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
public class Login extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
        try
        {
            LoginForm loginForm = (LoginForm)form;
	    String username = loginForm.getUsername();
	    String password = loginForm.getPassword();
	    User user = new UserDao().findByUserNamePassword(username, password);
            req.getSession().setAttribute("user", user);
            
            if (loginForm.getRemember())
            {
		LoginCookie lc = new LoginCookie(username, password);
		lc.setMaxAge(60 * 60 * 24 * 7); // Persist for one week.
		res.addCookie(lc);
	    }
	    
            return mapping.findForward("success");
        }
        catch (DataAccessException e)
        {
            throw new ServletException(e);
        }
    }
}

