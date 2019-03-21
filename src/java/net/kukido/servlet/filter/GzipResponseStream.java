/**
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 * This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 * http://www.jspbook.com. You may freely use the code both commercially
 * and non-commercially. If you like the code, please pick up a copy of
 * the book and help support the authors, development of more free code,
 * and the JSP/Servlet/J2EE community.
 *
 * (Modified 03/04/2004 by Chris Raser for style and functionality.)
 **/
package net.kukido.servlet.filter;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;

public class GzipResponseStream extends ServletOutputStream
{

    protected boolean closed = false;
    protected HttpServletResponse response = null;
    protected ServletOutputStream output = null;
    protected ByteArrayOutputStream baos = null;
    protected GZIPOutputStream gzipstream = null;
    
    public GzipResponseStream(HttpServletResponse response)
	throws IOException
    {
	this.closed = false;
	this.response = response;
	this.output = response.getOutputStream();
	this.baos = new ByteArrayOutputStream();
	this.gzipstream = new GZIPOutputStream(baos);
    }

    public void close() 
        throws IOException
    {
	gzipstream.finish();

	byte[] bytes = baos.toByteArray();

	response.addHeader("Content-Length", Integer.toString(bytes.length)); 
	output.write(bytes);
	output.flush();
	output.close();
	closed = true;
    }

    public void flush() throws IOException 
    {
	if (closed) {
	    throw new IOException("Cannot flush a closed output stream");
	}
	gzipstream.flush();
    }

    public void write(int b) throws IOException {
	if (closed) 
        {
	    throw new IOException("Cannot write to a closed output stream");
	}
	gzipstream.write((byte)b);
    }

    public void write(byte b[]) throws IOException 
    {
	write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) 
        throws IOException 
    {
	if (closed) {
	    throw new IOException("Cannot write to a closed output stream");
	}
	gzipstream.write(b, off, len);
    }

    public boolean closed() 
    {
	return closed;
    }
  
    /**
     * Does nothing.
     **/
    public void reset() {
	//noop
    }
}
