/*
 * Comment.java
 *
 * Created on February 21, 2006, 9:20 PM
 */

package net.kukido.blog.datamodel;

import java.util.Date;

/**
 * @author  craser
 */
public class Comment 
{
    private int commentId;
    private int entryId;
    private boolean isSpam;
    private String userName;
    private String userEmail;
    private String userUrl;
    private String comment;
    private Date datePosted;
    
    // These are entirely for Akismet's use.
    private String ipAddress;
    private String userAgent;
    private String referrer;
    
    public int getCommentId() {
        return commentId;
    }    

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    
     public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    
    public java.lang.String getUserName() {
        return userName;
    }
    
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    public java.lang.String getUserUrl() {
        return userUrl;
    }
    
    public void setUserUrl(java.lang.String userUrl) {
        this.userUrl = userUrl;
    }
    
    public java.lang.String getComment() {
        return comment;
    }
    
    public String getCommentHtml() {
        String commentHtml = comment.replaceAll("<[^>]*>", "");
        commentHtml = commentHtml.replaceAll("\r?\n\r?\n", "<p>");
        return commentHtml;
    }
    
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }
    
    public java.util.Date getDatePosted() {
        return datePosted;
    }
    
    public void setDatePosted(java.util.Date datePosted) {
        this.datePosted = datePosted;
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
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    /**
     * Getter for property isSpam.
     * @return Value of property isSpam.
     */
    public boolean isSpam() {
        return isSpam;
    }
    
    /**
     * Setter for property isSpam.
     * @param isSpam New value of property isSpam.
     */
    public void setIsSpam(boolean isSpam) {
        this.isSpam = isSpam;
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
     * Getter for property userAgent.
     * @return Value of property userAgent.
     */
    public java.lang.String getUserAgent() {
        return userAgent;
    }
    
    /**
     * Setter for property userAgent.
     * @param userAgent New value of property userAgent.
     */
    public void setUserAgent(java.lang.String userAgent) {
        this.userAgent = userAgent;
    }
    
    /**
     * Getter for property referrer.
     * @return Value of property referrer.
     */
    public java.lang.String getReferrer() {
        return referrer;
    }
    
    /**
     * Setter for property referrer.
     * @param referrer New value of property referrer.
     */
    public void setReferrer(java.lang.String referrer) {
        this.referrer = referrer;
    }
    
}
