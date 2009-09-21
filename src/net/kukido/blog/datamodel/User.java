package net.kukido.blog.datamodel;

import java.io.*;

public class User implements Serializable
{
    private int userId;
    private String userName;
    private String email;
    private String url;
    
    // The password field is a one-way channel for handing new
    // passwords to the back end.  It is NOT SET except when
    // creating or updating a user profile.
    private String password;

    public User() {}

    public User(String userName) {
	this.userName = userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getUserName() {
	return userName;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    /**
     * Getter for property email.
     * @return Value of property email.
     */
    public java.lang.String getEmail() {
        return email;
    }
    
    /**
     * Setter for property email.
     * @param email New value of property email.
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    
    /**
     * Getter for property url.
     * @return Value of property url.
     */
    public java.lang.String getUrl() {
        return url;
    }
    
    /**
     * Setter for property url.
     * @param url New value of property url.
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }
    
    /**
     * Getter for property password.
     * @return Value of property password.
     */
    public java.lang.String getPassword() {
        return password;
    }
    
    /**
     * Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
    
}
