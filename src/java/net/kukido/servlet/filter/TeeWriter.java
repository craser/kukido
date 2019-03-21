package net.kukido.servlet.filter;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TeeWriter extends PrintWriter
{
    private List<PrintWriter> writers;
    
    public TeeWriter(PrintWriter... writers) {
        super(writers[0]);
        this.writers = Arrays.asList(writers);
    }

    public PrintWriter append(char arg0)
    {
        for (PrintWriter writer : writers)
            writer.append(arg0);
        
        return this;
    }

    public PrintWriter append(CharSequence arg0, int arg1, int arg2)
    {
        for (PrintWriter writer : writers)
            writer.append(arg0, arg1, arg2);
        
        return this;
    }

    public PrintWriter append(CharSequence arg0)
    {
        for (PrintWriter writer : writers)
            writer.append(arg0);
        
        return this;
    }

    public boolean checkError()
    {
        boolean error = false;
        for (PrintWriter writer : writers)
            error |= writer.checkError();
        return error;
    }

    public void close()
    {
        for (PrintWriter writer : writers)
            writer.close();
    }

    public void flush()
    {
        for (PrintWriter writer : writers)
            writer.flush();
    }

    public PrintWriter format(Locale arg0, String arg1, Object... arg2)
    {
        for (PrintWriter writer : writers)
            writer.format(arg0, arg1, arg2);
        
        return this;
    }

    public PrintWriter format(String arg0, Object... arg1)
    {
        for (PrintWriter writer : writers)
            writer.format(arg0, arg1);
        
        return this;
    }

    public void print(boolean arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0);
    }

    public void print(char arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0);
    }

    public void print(char[] arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0);
    }

    public void print(double arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0);
    }

    public void print(float arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0); 
    }

    public void print(int arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0);
    }

    public void print(long arg0)
    {
        for (PrintWriter writer : writers)  
            writer.print(arg0);
    }

    public void print(Object arg0)
    {
        for (PrintWriter writer : writers)
            writer.print(arg0);
    }

    public void print(String arg0)
    {
        for (PrintWriter writer : writers)  
            writer.print(arg0);
    }

    public PrintWriter printf(Locale arg0, String arg1, Object... arg2)
    {
        for (PrintWriter writer : writers)
            writer.printf(arg0, arg1, arg2);
        
        return this;
    }

    public PrintWriter printf(String arg0, Object... arg1)
    {
        for (PrintWriter writer : writers)
            writer.printf(arg0, arg1);
        
        return this;
    }

    public void println()
    {
        for (PrintWriter writer : writers)
            writer.println();
    }

    public void println(boolean arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(char arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(char[] arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(double arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(float arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(int arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(long arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }   

    public void println(Object arg0)
    {
        for (PrintWriter writer : writers)
            writer.println(arg0);
    }

    public void println(String arg0)
    {
        for (PrintWriter writer : writers)
        writer.println(arg0);
    }

    public void write(char[] arg0, int arg1, int arg2)
    {
        for (PrintWriter writer : writers)
            writer.write(arg0, arg1, arg2);
    }

    public void write(char[] arg0)
    {
        for (PrintWriter writer : writers)      
            writer.write(arg0);
    }

    public void write(int arg0)
    {
        for (PrintWriter writer : writers)
            writer.write(arg0);
    }

    public void write(String arg0, int arg1, int arg2)
    {
        for (PrintWriter writer : writers)
            writer.write(arg0, arg1, arg2);
    }

    public void write(String arg0)
    {
        for (PrintWriter writer : writers)
            writer.write(arg0);
    }

}
