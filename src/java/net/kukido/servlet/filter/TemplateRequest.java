/*
 * TemplateRequest.java
 *
 * Created on October 25, 2003, 6:07 PM
 */

package net.kukido.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  craser
 */
public class TemplateRequest extends HttpServletRequestWrapper
{
    private String templateName;
    
    /** Creates a new instance of TemplateRequest */
    public TemplateRequest(ServletRequest req, String templateName)
    {
        super((HttpServletRequest)req);
        this.templateName = templateName;
    }
    
    public RequestDispatcher getRequestDispatcher(String path)
    {
        
        if (templateName != null)
        {
            RequestDispatcher dispatcher = super.getRequestDispatcher(templateName);
            templateName = null;
            return new TemplateDispatcher(dispatcher,  path);
        }
        else
        {
            return super.getRequestDispatcher(path);
        }
    }
}
