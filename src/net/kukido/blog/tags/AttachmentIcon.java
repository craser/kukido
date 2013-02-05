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
import net.kukido.blog.datamodel.*;

/**
 * @author  craser
 */
public class AttachmentIcon extends org.apache.struts.taglib.html.ImgTag
{
    static private final int DEFAULT_COMMENT_ID = -1;
    
    static public String PAGE = "page";
    static public String REQUEST = "request";
    static public String SESSION = "session";
    static public String APPLICATION = "application";
    
    private int scope;
    private String attachment;
    private Attachment.FileType attachmentType;
    private String comment;
    private int commentId = DEFAULT_COMMENT_ID;
    private boolean absolute; // Make this an absolute link?
    
    /** Creates a new instance of BaseHrefTag */
    public AttachmentIcon() 
    {
	this.scope = PageContext.PAGE_SCOPE;
    }
    
    public void setAttachment(String attachment)
    {
	this.attachment = attachment;
    }
    
    public void setAttachmentType(String attachmentType)
    {
    	setAttachmentType(Attachment.FileType.valueOf(attachmentType));
    }
    
    public void setAttachmentType(Attachment.FileType attachmentType)
    {
    	this.attachmentType = attachmentType;
    }
    
    public Attachment.FileType getAttachmentType()
    {
        return this.attachmentType;
    }
    
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
    public void setAbsolute(boolean absolute)
    {
	this.absolute = absolute;
    }

    public boolean getAbsolute()
    {
	return absolute;
    }

    public void setScope(String scope)
    {
        if (PAGE.equals(scope))
        {
            this.scope = PageContext.PAGE_SCOPE;
        }
        else if (SESSION.equals(scope))
        {
            this.scope = PageContext.SESSION_SCOPE;
        }
        else if (APPLICATION.equals(scope))
        {
            this.scope = PageContext.APPLICATION_SCOPE;
        }
        else if (REQUEST.equals(scope))
        {
            this.scope = PageContext.REQUEST_SCOPE;
        }
    }
    
    protected Attachment getAttachment()
    {
        return ((Attachment)pageContext.getAttribute(attachment, scope));    
    }

    public int doStartTag() throws JspException
    {
        setSrc();
        return super.doStartTag();
    }
    
	protected void setSrc() {
		try {
			if (attachment != null) {
				setAttachmentType(getAttachment().getFileType());
			}

			String type = getAttachmentType().toString();
			String path = "img/" + type + ".png";

			if (absolute)
				setSrc(getBaseUrl() + path);
			else
				setSrc(path);

			setAlt(type);
		} 
		catch (Exception ignored) {
			ignored.printStackTrace(System.out);
		}
	}


    /**
     * @return the base url of this page, including the trailing slash.
     **/
    protected String getBaseUrl()
	throws MalformedURLException
    {
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

	return baseUrl.toString();
    }

}
