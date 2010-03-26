/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.servlet.filter;

import javax.servlet.*;

/**
 *
 * @author craser
 */
public class ExceptionHandler implements Filter
{
    private String jsp; // Path to the JSP that will display error. 
    private boolean debug;
    
    public void destroy()
    {
    }
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws java.io.IOException, javax.servlet.ServletException
    {
        try {
            chain.doFilter(req, res);
        }
        catch(Exception e) {
            debug("Caught exeption #1", e);
            try {
                RequestDispatcher dispatcher = req.getRequestDispatcher(jsp);
                req.setAttribute("exception", e);
                dispatcher.forward(req, res);
            }
            catch (Exception ignored) {
                debug("Caught exception #2", ignored);
                // Nuthin' we can do.  We tried, son.
            }
        }
    }
    
    
    public void init(javax.servlet.FilterConfig config)
	throws ServletException
    {
        try {
            this.debug = Boolean.parseBoolean(config.getInitParameter("debug"));
            this.jsp = config.getInitParameter("jsp");
            debug("jsp: \"" + this.jsp + "\"");
        }
        catch (Exception e)
        {
            throw new ServletException("Error initializing " + getClass().getName(), e);
        }
    }

    private void debug(String message, Throwable t)
    {
	debug(message);
	if (debug) t.printStackTrace();
    }

    private void debug(String message)
    {
	if (debug) System.out.println("[ExceptionHandler] " + message);
    }

}
