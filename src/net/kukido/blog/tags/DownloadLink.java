package net.kukido.blog.tags;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by craser on 11/8/15.
 */
public class DownloadLink extends org.apache.struts.taglib.html.LinkTag
{
    private String format;
    private String fileName;

    public void setFormat(String format) {
        this.format = format;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int doStartTag() throws JspException {
        setHref();
        return super.doStartTag();
    }

    public void setHref() {
        String baseUrl = "";
        try { baseUrl = getBaseUrl(); }
        catch (Exception ignored) {}
        String href = baseUrl + "downloads/" + format + "/" + fileName;
        super.setHref(href);
    }


    /**
     * @return the base url of this page, including the trailing slash.
     * FIXME: This code is pasted from EntryLink. Need to refactor & share.
     **/
    public String getBaseUrl() throws MalformedURLException
    {
        HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
        ServletContext servContext = pageContext.getServletContext();
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
