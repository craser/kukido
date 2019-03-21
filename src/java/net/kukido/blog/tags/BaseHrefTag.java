/*
 * BaseHrefTag.java
 *
 * Created on September 12, 2004, 1:39 PM
 */

package net.kukido.blog.tags;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.net.*;

/**
 *
 * @author  craser
 */
public class BaseHrefTag implements Tag 
{
    private PageContext pageContext;
    private Tag parent;
    private boolean isXml = false; //contentType != null && contentType.toLowerCase().indexOf("xml") > -1;
    
    /** Creates a new instance of BaseHrefTag */
    public BaseHrefTag() {}
    
    public int doStartTag()
    {
        try {
            JspWriter out = pageContext.getOut();
            HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
            ServletContext servContext = (ServletContext)pageContext.getServletContext();
            URL reqUrl = new URL(req.getRequestURL().toString());
            
            
            String protocol = reqUrl.getProtocol();
            int port = reqUrl.getPort();
            String host = reqUrl.getHost();
            String webContext = servContext.getServletContextName();
            

            StringBuffer baseUrl = new StringBuffer();
            baseUrl.append(protocol);
            baseUrl.append("://");
            baseUrl.append(host);
            if (port >= 0) {
                baseUrl.append(":");
                baseUrl.append(port);
            }
            baseUrl.append(webContext.startsWith("/") ? "" : "/");
            baseUrl.append(webContext);
            baseUrl.append("/");
            
            //String contentType = pageContext.getResponse().getContentType(); // total hack.
            out.write("<base href=\"" 
                + baseUrl 
                + "\""
                + (isXml ? " /" : "") // Self-closing / only appropriate for XML
                + ">");            
            out.flush();
        }
        catch (Exception ignored) {
            ignored.printStackTrace(System.out);
        }
        
        return this.EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws javax.servlet.jsp.JspException {
        return EVAL_PAGE;
    }
    
    public javax.servlet.jsp.tagext.Tag getParent() {
        return parent;
    }
    
    public void release() {
    }
    
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }
    
    public void setParent(Tag tag) {
        this.parent = parent;
    }
    
    public void setIsXml(boolean isXml) {
    	this.isXml = isXml;
    }
}
