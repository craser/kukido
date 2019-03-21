package net.kukido.servlet.filter;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletOutputStream;

public class TeeServletOutputStream extends ServletOutputStream
{
    private ServletOutputStream servletOut;
    private PrintStream out;
    
    public TeeServletOutputStream(ServletOutputStream servletOut, PrintStream out)
    {
        this.servletOut = servletOut;
        this.out = out;
    }
    
    public void close() throws IOException
    {
        servletOut.close();
        out.close();
    }

    public void flush() throws IOException
    {
        servletOut.flush();
        out.flush();
    }

    public void print(boolean arg0) throws IOException
    {
        servletOut.print(arg0);
        out.print(arg0);
    }

    public void print(char c) throws IOException
    {
        servletOut.print(c);
        out.print(c);
    }

    public void print(double d) throws IOException
    {
        servletOut.print(d);
        out.print(d);
    }

    public void print(float f) throws IOException
    {
        servletOut.print(f);
        out.print(f);
    }

    public void print(int i) throws IOException
    {
        servletOut.print(i);
        out.print(i);
    }

    public void print(long l) throws IOException
    {
        servletOut.print(l);
        out.print(l);
    }

    public void print(String arg0) throws IOException
    {
        servletOut.print(arg0);
        out.print(arg0);
    }

    public void println() throws IOException
    {
        servletOut.println();
        out.println();
    }

    public void println(boolean b) throws IOException
    {
        servletOut.println(b);
        out.println(b);
    }

    public void println(char c) throws IOException
    {
        servletOut.println(c);
        out.println(c);
    }

    public void println(double d) throws IOException
    {
        servletOut.println(d);
        out.println(d);
    }

    public void println(float f) throws IOException
    {
        servletOut.println(f);
        out.println(f);
    }

    public void println(int i) throws IOException
    {
        servletOut.println(i);
        out.println(i);
    }

    public void println(long l) throws IOException
    {
        servletOut.println(l);
        out.println(l);
    }

    public void println(String s) throws IOException
    {
        servletOut.println(s);
        out.println(s);
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        servletOut.write(b, off, len);
        out.write(b, off, len);
    }

    public void write(byte[] b) throws IOException
    {
        servletOut.write(b);
        out.write(b);
    }

    public void write(int b) throws IOException
    {
        servletOut.write(b);
        out.write(b);
    }

}
