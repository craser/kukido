/*
 * GzipFilter.java
 *
 * Created on February 21, 2004, 5:36 AM
 */

package net.kukido.servlet.filter;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author  craser
 */
public class GzipFilter implements Filter
{
    static private final String HEADER_ACCEPT_ENCODING = "accept-encoding";
    static private final String GZIP = "gzip";

    /**
     * Checks the "accept-encoding" header of the incomming request for the
     * presence of the string "gzip".  If found, the request is wrapped in a 
     * GzipResponseWrapper before being passed on to chain.doFilter().  This
     * has the eventual effect of gzip-compressing output to the client.  See
     * documentation for GzipResponseWrapper for exactly how this is done.
     * @see net.kukido.blog.servlet.filter.GzipResponseWrapper
     * @see net.kukido.blog.servlet.filter.GzipResponseStream
     **/
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException
    {
        try
        {
            HttpServletRequest request = (HttpServletRequest)req;
            HttpServletResponse response = (HttpServletResponse)res;

            // check for the HTTP header that
            // signifies GZIP support
            String ae = request.getHeader(HEADER_ACCEPT_ENCODING);
            if (ae != null && ae.indexOf(GZIP) != -1)
            {
                GzipResponseWrapper wrappedResponse = new GzipResponseWrapper(response);
                chain.doFilter(req, wrappedResponse);
                return;
            }
            chain.doFilter(req, res);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw new ServletException(e);
        }
    }    
    
    /**
     * Does nothing.
     **/
    public void destroy() {}
    
    /**
     * Does nothing.
     **/
    public void init(javax.servlet.FilterConfig filterConfig) {}
    
}
