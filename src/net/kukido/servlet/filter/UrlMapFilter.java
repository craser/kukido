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

import javax.servlet.*;

import java.util.*;
import java.util.regex.*;


/**
 *
 * @author  craser
 */
public class UrlMapFilter implements Filter 
{
    static public final String PATTERN_PARAM = "pattern";
    static public final String TEMPLATE_PARAM = "template";
    
    private List mappings;
    private boolean debug;
    
    
    /** Creates a new instance of URLMapFilter */
    public UrlMapFilter()
    {
        debug = false;
    }
    
    public void destroy()
    {
    }
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws java.io.IOException, javax.servlet.ServletException
    {
        String requestUri = ((HttpServletRequest)req).getRequestURI();
        
        debug("Searching " + mappings.size() + " mappings for match for " + requestUri);

	boolean found = false;
	for (int i = 0; i < mappings.size(); i++)
	{
	    UrlMapping mapping = (UrlMapping)mappings.get(i);
	    if (mapping.matches(requestUri))
	    {
		String newUri = mapping.getResource(requestUri);
		debug("found mapping for URI[" + requestUri + "]: " + newUri);
		RequestDispatcher dsp = req.getRequestDispatcher(newUri);
		dsp.forward(req, res);
		found = true;
		break;
	    }
            else 
            {
                debug("Mapping " + mapping + " does not match URI \"" + requestUri + "\"");
            }
	}

	if (!found)
	{
	    debug("No mapping found for URI[" + requestUri + "]");
            chain.doFilter(req, res);
        }
    }
    
    public void init(javax.servlet.FilterConfig config)
	throws javax.servlet.ServletException
    {
        debug = new Boolean(config.getInitParameter("debug")).booleanValue();
        
	mappings = new ArrayList();
	for (int i = 1; config.getInitParameter(PATTERN_PARAM + i) != null; i++)
	{
	    String pattern = config.getInitParameter(PATTERN_PARAM + i);
	    String template  = config.getInitParameter(TEMPLATE_PARAM + i);
            UrlMapping mapping = new UrlMapping(pattern, template);
            debug("[UrlMapFilter] adding: " + mapping);
	    mappings.add(mapping);
	}
	
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
