package net.kukido.blog.tags;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.kukido.blog.datamodel.Attachment;

import org.apache.struts.taglib.html.ImgTag;

public class Image extends ImgTag
{
    static private final String URL_FORMAT = "attachments/{0}/{1}";
    
    static public String PAGE = "page";
    static public String REQUEST = "request";
    static public String SESSION = "session";
    static public String APPLICATION = "application";
    
    private int scope;
    private String size; // thumbnail, postcard, postcard, desktop, raw
    private String fileName;
    private String attachment;
    private boolean absolute = false;

    public void setSize(String size)
    {
        this.size = size;
    }

    public int doStartTag() throws JspException
    {
        setSrc();
        return super.doStartTag();
    }
    
    protected void setSrc()
    {
        try {
            if (attachment != null) {
                setFileName(getAttachment().getFileName());
            }
            
            String src = MessageFormat.format(URL_FORMAT, size, fileName);
                
            if (absolute) {
                setSrc(getBaseUrl() + src);
            }
            else {
                setSrc(src);
            }
        }
        catch (Exception ignored) {
            ignored.printStackTrace(System.out);
        }
    }
    
    protected Attachment getAttachment()
    {
        return ((Attachment)pageContext.getAttribute(attachment, scope));    
    }

    /**
     * @return the base url of this page, including the trailing slash.  
     * This code is IDENTICAL to that found in EntryLink.java, and should
     * remain so until I can figure out a graceful way to refactor this.
     **/
    protected String getBaseUrl() throws MalformedURLException
    {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        ServletContext servContext = (ServletContext) pageContext.getServletContext();
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

    public String getSize()
    {
        return size;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public void setAbsolute(boolean absolute)
    {
        this.absolute = absolute;
    }

    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
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

}
