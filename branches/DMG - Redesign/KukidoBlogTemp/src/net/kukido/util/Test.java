package net.kukido.util;

import java.io.*;

public class Test
{
    public static void main(String[] args)
	throws Exception
    {
	FileLog log = new FileLog(new File("events.log"));

	// Log a few errors
	for (int i = 0; i < 2; i++)
	{
	    log.log(Log.SEVERITY_ERROR, new NullPointerException("I can't find me!"));
	}

	// Log a warning
	log.log(Log.SEVERITY_WARNING, "NO VARIABLES BOUND");

	// Log a debugging message
	log.log(Log.SEVERITY_DEBUG, "args: " + args);

	log.close();
    }
}
	
