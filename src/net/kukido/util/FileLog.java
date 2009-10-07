package net.kukido.util;

import java.io.*;

public class FileLog extends Log
{
    private Writer out;

    //////////////////////////////////////////////////////////////////////
    // Constructors

    public FileLog(File logFile)
	throws IOException
    {
	this(logFile, new LogFormat());
    }

    public FileLog(File logFile, LogFormat logFormat)
	throws IOException
    {
	super(logFormat);
	this.out = new FileWriter(logFile);
    }

    //////////////////////////////////////////////////////////////////////
    // Logging

    public void log(LogEvent event)
    {
	try { out.write(getLogFormat().formatEvent(event)); }
	catch (IOException e) {}
	super.log(event);
    }

    //////////////////////////////////////////////////////////////////////
    // Housekeeping
    public void close()
	throws IOException
    {
	out.flush();
	out.close();
    }


    public void finalize()
    {
	try { close(); }
	catch (Exception ignored) {}
    }
	
}
