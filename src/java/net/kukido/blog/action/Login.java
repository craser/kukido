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
import net.kukido.blog.log.Logging;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

/**
 * 
 * @author craser
 * @version
 */
public class Login extends Action 
{
	private final Logger log = Logging.getLogger(getClass());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException 
	{
		LoginForm loginForm = (LoginForm) form;
		String username = loginForm.getUsername();
		String password = loginForm.getPassword();
		try {
			User user = new UserDao().findByUserNamePassword(username, password);
			log.info("User " + username + " logged in.");
			req.getSession().setAttribute("user", user);

			if (loginForm.getRemember()) {
				LoginCookie lc = new LoginCookie(username, password);
				lc.setMaxAge(60 * 60 * 24 * 7); // Persist for one week.
				log.debug("Setting login cookie: " + lc);
				res.addCookie(lc);
			}

			log.debug("Login successful.");
			return mapping.findForward("success");
		} 
		catch (DataAccessException e) {
			log.info("Login for user " + username + " unsucessful.", e);
			throw new ServletException(e);
		}
	}
}
