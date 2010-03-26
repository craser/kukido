/*
 * Duration.java
 *
 * Created on March 3, 2008, 10:11 PM
 */

package net.kukido.blog.tags;

/**
 * Formats a length of time in milliseconds as dd:hh:mm:ss
 * @author  craser
 */
public class Duration extends org.apache.struts.taglib.bean.WriteTag
{
    private long millis;
    private String format;
    
    /** Creates a new instance of Duration */
    public Duration() {
    }
    
    /**
     * Getter for property millis.
     * @return Value of property millis.
     */
    public long getMillis() {
        return millis;
    }    
    
    /**
     * Setter for property millis.
     * @param millis New value of property millis.
     */
    public void setMillis(long millis) {
        this.millis = millis;
    }
    
    /**
     * Getter for property format.
     * @return Value of property format.
     */
    public java.lang.String getFormat() {
        return format;
    }
    
    /**
     * Setter for property format.
     * @param format New value of property format.
     */
    public void setFormat(java.lang.String format) {
        this.format = format;
    }
    
}
