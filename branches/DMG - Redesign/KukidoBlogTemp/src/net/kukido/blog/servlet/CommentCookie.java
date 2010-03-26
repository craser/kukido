package net.kukido.blog.servlet;

import javax.servlet.http.*;
import java.util.regex.*;

public class CommentCookie extends Cookie
{
    static public String COOKIE_NAME = "cmtcookie";
    private String userName;
    private String email;
    private String url;

    /**
     * @return The Cookie (if present) matching the name of CommentCookie.COOKIE_NAME
     *
     * @throws NullPointerException If that cookie cannot be found.
     **/
    static public CommentCookie getCommentCookie(HttpServletRequest req)
    {
	Cookie[] cookies = req.getCookies();
	for (int i = 0 ; i < cookies.length; i++) {
	    if (COOKIE_NAME.equals(cookies[i].getName()))
	    {
		return new CommentCookie(cookies[i]);
	    }
	}

	throw new NullPointerException("No cookie found with name \"" + COOKIE_NAME + "\"");
    }

    public CommentCookie(Cookie cookie)
    {
	super(cookie.getName(), cookie.getValue());
	setComment(cookie.getComment());
	if (cookie.getDomain() != null) setDomain(cookie.getDomain());
	setMaxAge(cookie.getMaxAge());
	setPath(cookie.getPath());
	setSecure(cookie.getSecure());

	Matcher nameMatch = Pattern.compile("userName=([\\w ]*)").matcher(cookie.getValue());
	Matcher emailMatch = Pattern.compile("email=([\\w@\\._ ]*)").matcher(cookie.getValue());
        Matcher urlMatch = Pattern.compile("url=(.*$)").matcher(cookie.getValue());

	if (nameMatch.find() && emailMatch.find() && urlMatch.find())
	{
	    this.userName = nameMatch.group(1);
	    this.email = emailMatch.group(1);
            this.url = urlMatch.group(1);
	    super.setValue(formatCookieValue(userName, email, url));
	}
	else
	{
	    throw new IllegalArgumentException("Cookie not a correctly formatted login cookie!");
	}
    }

    public CommentCookie(String userName, String email, String url)
    {
	super(COOKIE_NAME, null);
	this.userName = userName;
	this.email = email;
        this.url = url;
	super.setValue(formatCookieValue(userName, email, url));
    }

    public void setUserName(String userName)
    {
	this.userName = userName;
	super.setValue(formatCookieValue(userName, email, url));
    }

    public String getUserName()
    {
	return userName;
    }

    public void setEmail(String email)
    {
	this.email = email;
	super.setValue(formatCookieValue(userName, email, url));
    }

    public String getEmail()
    {
	return email;
    }
    
    

    /**
     * Overrides super.setValue()
     **/
    public void setValue() {
	throw new UnsupportedOperationException("Cannot directly set value of LoginCookie!");
    }

    private String formatCookieValue(String userName, String email, String url)
    {
	return "userName=" + userName + "&email=" + email + "&url=" + url;
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
        super.setValue(formatCookieValue(userName, email, url));
    }
    
}
