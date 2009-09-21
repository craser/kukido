package net.kukido.util;

import java.io.*;
import java.util.*;

/**
 * The Log class provides a convenient means of logging events.
 */
public class Log
{
    public static final int SEVERITY_ERROR = 1;
    public static final int SEVERITY_WARNING = 2;
    public static final int SEVERITY_DEBUG = 3;

    private List events;
    private LogFormat logFormat;

    //////////////////////////////////////////////////////////////////////
    // Constructors

    public Log()
    {
	this(new LogFormat());
    }

    public Log(LogFormat logFormat)
    {
	this.logFormat = logFormat;
	this.events = Collections.synchronizedList(new ArrayList());
    }

    //////////////////////////////////////////////////////////////////////
    // Logging

    public void log(int severity, String message)
    {
	log(severity, message, null);
    }

    public void log(int severity, Throwable throwable)
    {
	log(severity, throwable.getMessage(), throwable);
    }

    public void log(int severity, String message, Throwable throwable)
    {
	log(new LogEvent(severity, message, throwable));
    }

    public void log(LogEvent event)
    {
	events.add(event);
    }

    //////////////////////////////////////////////////////////////////////
    // Accessors
    public LogFormat getLogFormat()
    {
	return logFormat;
    }
    
    public List getEvents()
    {
	return events;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Output

    public void print(Writer out)
	throws IOException
    {
	out.write(logFormat.formatLog(this));
    }
    
    public String toString()
    {
	return logFormat.formatLog(this);
    }
}
