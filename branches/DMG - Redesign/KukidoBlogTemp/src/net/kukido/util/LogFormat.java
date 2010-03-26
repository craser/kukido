package net.kukido.util;

import java.io.*;
import java.text.*;
import java.util.*;
import net.kukido.io.*;

/**
 * Represents the format to be used to output log events to some kind
 * of output stream.
 */
public class LogFormat implements Serializable
{
    private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd kk:mm:ss";
    private static final String SEVERITY_ERROR   = "ERROR  ";
    private static final String SEVERITY_WARNING = "WARNING";
    private static final String SEVERITY_DEBUG   = "DEBUG  ";

    private String dateFormat;

    public LogFormat()
    {
	this(DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Creates a new LogFormat with the given dateFormat
     *
     * @param dateFormat follows the rules for date formatting by
     * java.text.SimpleDateFormat
     */
    public LogFormat(String dateFormat)
    {
	this.dateFormat = dateFormat;
    }

    /**
     * Sets the date format.
     *
     * @param dateFormat follows the rules for date formatting by
     * java.text.SimpleDateFormat
     */
    public void setDateFormat(String dateFormat)
    {
	this.dateFormat = dateFormat;
    }
    

    /**
     * Returns a formatted event suitable for printing to a log file.
     * (May be multiple-lines long.)
     */
    public String formatLog(Log log)
    {
	StringWriter out = new StringWriter();
	for (Iterator events = log.getEvents().iterator(); events.hasNext();) {
	    out.write(formatEvent((LogEvent)events.next()));
	}
	return out.toString();
    }	

    /**
     * Returns a formatted event suitable for printing to a log file.
     * (May be multiple-lines long.)
     */
    public String formatEvent(LogEvent event)
    {
	SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	StringWriter stringWriter = new StringWriter();
	IndentedWriter indent = new IndentedWriter(stringWriter);
	PrintWriter out = new PrintWriter(indent);

	// First line formatted as: <date> <severity> <message>
	out.print(date.format(new Date(event.getTimestamp())));
	out.print(" ");
	out.print("[SEVERITY: " + getSeverity(event.getSeverity()) + "]");
	out.print(" ");
	out.print(event.getMessage());
	out.println(""); // newline.

	// Subsequent lines indented
	if (null != event.getThrowable())
	{
	    indent.push();
	    event.getThrowable().printStackTrace(out);
	    indent.pop();
	}

	return stringWriter.toString();
    }

    /**
     * Converts a integer representation of an event's severity to a string.
     *
     * @param severity must be a severity rating from dmg.util.Log
     */
    public String getSeverity(int severity)
    {
	String result = "";
	switch (severity)
	{
	case Log.SEVERITY_ERROR:
	    result = SEVERITY_ERROR;
	    break;
	case Log.SEVERITY_WARNING:
	    result = SEVERITY_WARNING;
	    break;
	case Log.SEVERITY_DEBUG:
	    result = SEVERITY_DEBUG;
	    break;
	default:
	    throw new IllegalArgumentException("Unrecognised severity rating: " + severity);
	}
	return result;
    }
}
