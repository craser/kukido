/*
 * TemplateDispatcher.java
 *
 * Created on October 25, 2003, 6:12 PM
 */

package net.kukido.servlet.filter;

import javax.servlet.*;
/**
 *
 * @author  craser
 */
public class TemplateDispatcher implements RequestDispatcher
{
    private String content;
    private RequestDispatcher dispatcher;
    
    /** Creates a new instance of TemplateDispatcher */
    public TemplateDispatcher(RequestDispatcher dispatcher, String content)
    {
        this.dispatcher = dispatcher;
        this.content = content;
    }
    
    public void forward(ServletRequest req, ServletResponse res) 
    throws javax.servlet.ServletException, java.io.IOException
    {
        req.setAttribute("contentPage", content);
        dispatcher.forward(req, res);
    }
    
    public void include(ServletRequest req, ServletResponse res) 
    throws javax.servlet.ServletException, java.io.IOException
    {
        req.setAttribute("contentPage", content);
        dispatcher.include(req, res);
    }
    
}
