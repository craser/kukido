/*
 * TemplateFilter.java
 *
 * Created on October 25, 2003, 5:57 PM
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
 * @version
 */

public class TemplateFilter implements Filter
{
    private String templateName;
    
    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig)
    {
        this.templateName = filterConfig.getInitParameter("templateName");
    }   
    
    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
    {
        response.setContentType("text/html");
        TemplateRequest templateRequest = new TemplateRequest(request, templateName);
        chain.doFilter(templateRequest, response);
    }
    
    
    
    /**
     * Destroy method for this filter
     *
     */
    public void destroy()
    {
    }

}
