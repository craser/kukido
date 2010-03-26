package net.kukido.blog.servlet.filter;

import net.kukido.blog.security.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This Filter screens incomming requests to log in users.
 *
 * If the user is already logged in, this filter does nothing.
 *
 * If the user is not logged in, this Filter checks for a cookie with
 * the username and password stored in it, and uses this information
 * to log the user in.
 *
 * Any exceptions generated during this process are ignored.  In call
 * cases, this Filter passes control to the next item in the
 * FilterChain.
 **/
public class LoginFilter implements Filter
{

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException
    {
	HttpServletRequest httpReq = (HttpServletRequest)req;
        HttpServletResponse httpRes = (HttpServletResponse)res;
	try
	{
	    User user = (User)httpReq.getSession().getAttribute("user");
	    if (user == null)
	    {
		LoginCookie loginCookie = LoginCookie.getLoginCookie(httpReq);
		String username = loginCookie.getUsername();
		String password = loginCookie.getPassword();
		user = new UserDao().findByUserNamePassword(username, password);
		httpReq.getSession().setAttribute("user", user);
                
		loginCookie.setMaxAge(60 * 60 * 24 * 7); // Persist for another week.
                httpRes.addCookie(loginCookie);
	    }
	}
	catch (Exception ignored)
	{
            //ignored.printStackTrace(System.out);
	}
	finally
	{
	    try { 
                chain.doFilter(req, res); 
            }
            catch (ServletException e) {
                e.printStackTrace(System.out);
                throw e;
            }
            catch (IOException e) {
                e.printStackTrace(System.out);
                throw e;
            }
            catch (RuntimeException e) {
                e.printStackTrace(System.out);
                throw e;
            }
	}
    }

    /**
     * No-op.
     **/
    public void init(FilterConfig config) {}

    /**
     * No-op.
     **/
    public void destroy() {}
}
