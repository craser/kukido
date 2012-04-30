package net.kukido.blog.log;

import org.apache.log4j.*;

public class Logging
{
    static public Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz.getCanonicalName());
    }
    
    static public Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

}
