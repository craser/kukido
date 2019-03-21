/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.servlet.filter;

import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Used to detect multiple requests from the same
 * source host to the same URL.
 * @author craser
 */
public class RequestProfile {
    private String uri;
    private String sourceIp;
    private String method;
    private Date timestamp; // Used internally only.
    private long expires;
    private long delay;
    
    public RequestProfile(ServletRequest req)
    {
        this((HttpServletRequest)req);
    }
    
    public RequestProfile(HttpServletRequest req)
    {
        this.uri = req.getRequestURI();
        this.sourceIp = req.getRemoteAddr();
        this.method = req.getMethod();
        this.timestamp = new Date();
    }
    
    public void setDelay(long delay)
    {
        this.delay = delay;
        this.expires = timestamp.getTime() + delay;
    }
    
    public long getDelay()
    {
        return this.delay;
    }
    
    public long getExpires()
    {
        return expires;
    }
    
    public boolean equals(Object o)
    {
        if (!(o instanceof RequestProfile))
            return false;
        
        RequestProfile rp = (RequestProfile)o;
        return this.uri.equals(rp.uri) 
                && this.sourceIp.equals(rp.sourceIp)
                && this.method.equals(rp.method);
    }
    
    public int hashCode()
    {
        return toComparisonString().hashCode();
    }
    
    public String toString()
    {
        return "[" + timestamp + "]" + toComparisonString();
    }
    
    public String toComparisonString()
    {
        StringBuffer b = new StringBuffer();
        b.append("[");
        b.append(method);
        b.append("][");
        b.append(sourceIp);
        b.append("]");
        b.append(uri);
        
        return b.toString();
    }
}
