/*
 * DmgConfig.java
 *
 * Created on May 23, 2004, 11:29 PM
 */

package net.kukido.blog.config;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author  craser
 */
public class DmgConfig extends java.util.Properties 
{
    static public final String PROPERTIES_NAME = "datasource.properties";
    
    /** Creates a new instance of DmgConfig */
    public DmgConfig()
    {
        try { 
            load(getClass().getResourceAsStream(PROPERTIES_NAME));
        }
        catch (Throwable e) 
        {
            e.printStackTrace(System.out);            
        }
    }
    
    public void store()
        throws IOException
    {
        FileOutputStream propsOut = null;
        try
        {
            URL url = getClass().getResource(PROPERTIES_NAME);
            URI uri = new URI(url.toString()); // Hackety-hackety hack
            File propsFile = new File(uri);
            propsOut = new FileOutputStream(propsFile);
            this.store(propsOut, "Saved: " + new Date());
        }
        catch (URISyntaxException e)
        {
            throw new IOException("Unable to construct URI");
        }
        finally
        {
            try { propsOut.close(); } catch (Exception ignored) {}
        }
    }
}
