/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.servlet.filter;

import javax.servlet.*;

import net.kukido.blog.log.Logging;

import org.apache.log4j.Logger;

/**
 *
 * @author craser
 */
public class ExceptionHandler implements Filter
{
    private Logger log;
    private String jsp; // Path to the JSP that will display error. 
    
    public void destroy()
    {
    }
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws java.io.IOException, javax.servlet.ServletException
    {
        try {
            log.info("Filtering.");
            log.debug("Calling down the chain.");
            chain.doFilter(req, res);
        }
        catch(Exception e) {
            log.error("Caught exeption #1", e);
            try {
                RequestDispatcher dispatcher = req.getRequestDispatcher(jsp);
                req.setAttribute("exception", e);
                dispatcher.forward(req, res);
            }
            catch (Exception ignored) {
                log.error("Caught exception #2", ignored);
                // Nuthin' we can do.  We tried, son.
            }
        }
    }
    
    
    public void init(javax.servlet.FilterConfig config)
	throws ServletException
    {
        try {
            this.log = Logging.getLogger(getClass());
            this.jsp = config.getInitParameter("jsp");
            log.debug("Error jsp: \"" + this.jsp + "\"");
        }
        catch (Exception e)
        {
            throw new ServletException("Error initializing " + getClass().getName(), e);
        }
    }

}
