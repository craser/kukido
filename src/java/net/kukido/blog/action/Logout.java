/*
 * Logout.java
 *
 * Created on October 26, 2003, 1:17 AM
 */

package net.kukido.blog.action;

import net.kukido.blog.security.*;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author  craser
 * @version
 */
public class Logout extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
    {
        req.getSession().setAttribute("user", null);
	removeLoginCookie(req, res);
        return mapping.findForward("success");
    }

    private void removeLoginCookie(HttpServletRequest req, HttpServletResponse res)
    {
	try
	{
	    LoginCookie loginCookie = LoginCookie.getLoginCookie(req);
	    loginCookie.setMaxAge(0); // 0 value deletes the cookie
	    res.addCookie(loginCookie);
	}
	catch (Exception ignored) {}
    }
}
