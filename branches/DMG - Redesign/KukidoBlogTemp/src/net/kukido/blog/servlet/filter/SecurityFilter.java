/*
 * SecurityFilter.java
 *
 * Created on October 25, 2003, 4:05 PM
 */

package net.kukido.blog.servlet.filter;

import net.kukido.blog.security.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  craser
 * @version
 */

public class SecurityFilter implements Filter
{
    private String loginPage;
    
    /**
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException
    {
        HttpServletRequest httpReq = (HttpServletRequest)req;
        try
        {
            if (httpReq.getSession().getAttribute("user") == null)
	    {
		throw new ServletException("User not logged in!");
	    }
            chain.doFilter(req, res);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            RequestDispatcher dispatcher = req.getRequestDispatcher(loginPage);
            dispatcher.forward(req, res);
        }
    }

    public void destroy()
    {
    }
    
    public void init(javax.servlet.FilterConfig filterConfig) 
	throws javax.servlet.ServletException
    {
        loginPage = filterConfig.getInitParameter("loginPage");
    }
}


