/*
 * URLMapFilter.java
 *
 * Created on September 11, 2004, 10:45 AM
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

import java.util.*;
import java.util.regex.*;


/**
 *
 * @author  craser
 */
public class UrlRedirectFilter implements Filter 
{
    static public final String PATTERN_PARAM = "pattern";
    static public final String TEMPLATE_PARAM = "template";
    private UrlMapping mapping;
    private boolean debug;
    
    
    /** Creates a new instance of URLMapFilter */
    public UrlRedirectFilter()
    {
        debug = false;
    }
    
    public void destroy()
    {
    }
    
    /**
     * WARNING: Assumes that res is an HttpServletRequest.
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws java.io.IOException, javax.servlet.ServletException
    {
        String requestUri = ((HttpServletRequest)req).getRequestURI();
	if (mapping.matches(requestUri))
	{
            String newUri = mapping.getResource(requestUri);
            debug("found mapping for URI[" + requestUri + "]: " + newUri);
            //RequestDispatcher dsp = req.getRequestDispatcher(newUri);
            //dsp.forward(req, res);
            
            ((HttpServletResponse)res).sendRedirect(newUri); // BEWARE CASTING ERRORS!
	}
        else
	{
	    debug("no mapping found for URI \"" + requestUri + "\"");
            chain.doFilter(req, res);
        }
    }
    
    public void init(javax.servlet.FilterConfig config)
	throws javax.servlet.ServletException
    {
	String pattern = config.getInitParameter(PATTERN_PARAM);
	String template  = config.getInitParameter(TEMPLATE_PARAM);
        mapping = new UrlMapping(pattern, template);
	
        debug = new Boolean(config.getInitParameter("debug")).booleanValue();
    }

    private void debug(String message, Throwable t)
    {
	debug(message);
	if (debug) t.printStackTrace();
    }

    private void debug(String message)
    {
	if (debug) System.out.println(message);
    }
}
