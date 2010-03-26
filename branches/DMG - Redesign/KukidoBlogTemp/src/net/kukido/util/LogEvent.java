package net.kukido.util;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * This class represents a loggable event.  (See dmg.util.Log) Legal
 * values for severity integer may be found in dmg.util.Log.
 *
 * It holds for pieces of information:
 *
 * 1. A timestamp
 * 2. A severity rating (@see dmg.util.Log)
 * 3. A message explaining the nature of the event.
 * 4. A Throwable associated with the event.
 */
public class LogEvent implements Serializable
{
    private int severity;        // See legal values in dmg.util.Log
    private Throwable throwable; // Cause of the error
    private String message;      // Explanation of the event
    private long timestamp;      // Time of the event

    //////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new event to be logged. 
     * 
     * @param severity must be a severity rating from dmg.util.Log
     */
    public LogEvent(int severity, String message)
    {
	if (severity != Log.SEVERITY_ERROR
	    && severity != Log.SEVERITY_WARNING
	    && severity != Log.SEVERITY_DEBUG)
	{
	    throw new IllegalArgumentException("Unrecognised severity rating: " + severity);
	}
	
	this.timestamp = new Date().getTime();
	this.severity = severity;
	this.message = message;
    }

    /**
     * Creates a new event to be logged. 
     * 
     * @param severity must be a severity rating from dmg.util.Log
     */
    public LogEvent(int severity, Throwable throwable)
    {
	this(severity, throwable.getMessage(), throwable);
    }

    /**
     * Creates a new event to be logged. 
     * 
     * @param severity must be a severity rating from dmg.util.Log
     */
    public LogEvent(int severity, String message, Throwable throwable)
    {
	this(severity, message);
	this.throwable = throwable;
    }

    //////////////////////////////////////////////////////////////////////
    // Accessors

    public int getSeverity()
    {
	return severity;
    }

    public String getMessage()
    {
	return message;
    }

    public Throwable getThrowable()
    {
	return throwable;
    }

    public long getTimestamp()
    {
	return timestamp;
    }
}
