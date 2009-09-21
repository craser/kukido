package net.kukido.blog.security;

import javax.servlet.http.*;
import java.util.regex.*;

public class LoginCookie extends Cookie
{
    static public String COOKIE_NAME = "login";
    private String username;
    private String password;

    /**
     * @return The Cookie (if present) matching the name of LoginCookie.COOKIE_NAME
     *
     * @throws NullPointerException If that cookie cannot be found.
     **/
    static public LoginCookie getLoginCookie(HttpServletRequest req)
    {
	Cookie[] cookies = req.getCookies();
	for (int i = 0 ; i < cookies.length; i++) {
	    if (COOKIE_NAME.equals(cookies[i].getName()))
	    {
		return new LoginCookie(cookies[i]);
	    }
	}

	throw new NullPointerException("No cookie found with name \"" + COOKIE_NAME + "\"");
    }

    public LoginCookie(Cookie cookie)
    {
	super(cookie.getName(), cookie.getValue());
	setComment(cookie.getComment());
	if (cookie.getDomain() != null) setDomain(cookie.getDomain());
	setMaxAge(cookie.getMaxAge());
	setPath(cookie.getPath());
	setSecure(cookie.getSecure());

	Matcher userMatch = Pattern.compile("username=(\\w*)").matcher(cookie.getValue());
	Matcher passMatch = Pattern.compile("password=(\\w*)").matcher(cookie.getValue());

	if (userMatch.find() && passMatch.find())
	{
	    this.username = userMatch.group(1);
	    this.password = passMatch.group(1);
	    super.setValue(formatCookieValue(username, password));
	}
	else
	{
	    throw new IllegalArgumentException("Cookie not a correctly formatted login cookie!");
	}
    }

    public LoginCookie(String username, String password)
    {
	super(COOKIE_NAME, null);
	this.username = username;
	this.password = password;
	super.setValue(formatCookieValue(username, password));
    }

    public void setUsername(String username)
    {
	this.username = username;
	super.setValue(formatCookieValue(username, password));
    }

    public String getUsername()
    {
	return username;
    }

    public void setPassword(String password)
    {
	this.password = password;
	super.setValue(formatCookieValue(username, password));
    }

    public String getPassword()
    {
	return password;
    }

    /**
     * Overrides super.setValue()
     **/
    public void setValue() {
	throw new UnsupportedOperationException("Cannot directly set value of LoginCookie!");
    }

    private String formatCookieValue(String username, String password)
    {
	return "username=" + username + "&password=" + password;
    }
}
