package net.kukido.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class GzipResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper
{
    private GzipResponseStream gzipStream;
    private ServletOutputStream output;
    
    public GzipResponseWrapper(HttpServletResponse res)
        throws IOException
    {
        super(res);
        this.output = res.getOutputStream();
        this.gzipStream = new GzipResponseStream(res);
	res.addHeader("Content-Encoding", "gzip");
    }

    public ServletOutputStream getOutputStream()
    {
        return this.gzipStream;
    }
    
    public PrintWriter getWriter()
    {
        return new PrintWriter(gzipStream);
    }
}
