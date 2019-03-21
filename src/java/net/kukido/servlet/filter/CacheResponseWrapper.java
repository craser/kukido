package net.kukido.servlet.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CacheResponseWrapper extends HttpServletResponseWrapper
{
    private int httpStatus = HttpServletResponse.SC_OK;
    private PrintWriter fileWriter;
    private PrintStream fileStream;
    
    private ServletOutputStream servletOut;
    private PrintWriter printWriter;

    public CacheResponseWrapper(HttpServletResponse response, PrintWriter writer, PrintStream stream)
    {
        super(response);
        this.fileWriter = writer;
        this.fileStream = stream;
    }
    
    public int getHttpStatus() {
        return this.httpStatus;
    }
    
    public void sendError(int status) throws IOException {
        httpStatus = status;
        super.sendError(status);
    }
    
    public void sendError(int status, String message) throws IOException {
        httpStatus = status;
        super.sendError(status, message);
    }
    
    public void setStatus(int status) {
        httpStatus = status;
        super.setStatus(status);
    }
    
    public void setStatus(int status, String message) {
        httpStatus = status;
        super.setStatus(status, message);
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        servletOut = (servletOut == null)
                ? new TeeServletOutputStream(super.getOutputStream(), fileStream)
                : servletOut;
                
        return servletOut;
    }
    
    public PrintWriter getWriter() throws IOException{
        printWriter = (printWriter == null) 
                ? new TeeWriter(super.getWriter(), fileWriter)
                : printWriter;
        
        return printWriter;
    }
}
