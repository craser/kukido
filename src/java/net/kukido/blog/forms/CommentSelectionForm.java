package net.kukido.blog.forms;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.util.*;


/**
 * Wraps up criteria for selecting Comments
 */
public class CommentSelectionForm extends SearchForm
{
    // Note that if any of these default Strings are changed to non-null
    // values, you must alter getParamMap() to use equals() instead of 
    // !=
    static private final boolean DEFAULT_SPAM = false;
    static private final String DEFAULT_IP_ADDRESS = null;
    static private final String DEFAULT_USER_NAME = null;
    static private final String DEFAULT_USER_EMAIL = null;
    static private final int DEFAULT_USER_ID = -1;
    
    private boolean spam;
    private String ipAddress;
    private String userName;
    private String userEmail;
    private int entryId;
    
    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
        super.reset(mapping, req);
        this.entryId = DEFAULT_USER_ID;
        this.spam = DEFAULT_SPAM;
        this.ipAddress = DEFAULT_IP_ADDRESS;
        this.userName = DEFAULT_USER_NAME;
        this.userEmail = DEFAULT_USER_EMAIL;        
    }
    
    /**
     * Getter for property spam.
     * @return Value of property spam.
     */
    public boolean isSpam() {
        return spam;
    }
    
    /**
     * Setter for property spam.
     * @param spam New value of property spam.
     */
    public void setSpam(boolean spam) {
        this.spam = spam;
    }
    
    /**
     * Getter for property ipAddress.
     * @return Value of property ipAddress.
     */
    public java.lang.String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * Setter for property ipAddress.
     * @param ipAddress New value of property ipAddress.
     */
    public void setIpAddress(java.lang.String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    /**
     * Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    
    /**
     * Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    /**
     * Getter for property userEmail.
     * @return Value of property userEmail.
     */
    public java.lang.String getUserEmail() {
        return userEmail;
    }
    
    /**
     * Setter for property userEmail.
     * @param userEmail New value of property userEmail.
     */
    public void setUserEmail(java.lang.String userEmail) {
        this.userEmail = userEmail;
    }
    
    /** 
     * Overrides super.getParamMap()
     */
    public Map getParamMap()
    {
        Map m = super.getParamMap();
        if (spam != DEFAULT_SPAM) m.put("spam", Boolean.toString(spam));
        if (ipAddress != DEFAULT_IP_ADDRESS) m.put("ipAddress", ipAddress);
        if (userEmail != DEFAULT_USER_EMAIL) m.put("userEmail", userEmail);
        
        return m;
    }    
    
    /**
     * Getter for property entryId.
     * @return Value of property entryId.
     */
    public int getEntryId() {
        return entryId;
    }
    
    /**
     * Setter for property entryId.
     * @param entryId New value of property entryId.
     */
    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    
}
