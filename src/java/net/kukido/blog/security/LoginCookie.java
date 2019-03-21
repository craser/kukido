package net.kukido.blog.security;

import java.text.*;
import javax.servlet.http.*;

public class LoginCookie extends Cookie
{
    static public String COOKIE_NAME = "login";
    static private final MessageFormat cookieFormat = new MessageFormat("user={0}&pass={1}");
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
        try {
            setComment(cookie.getComment());
            if (cookie.getDomain() != null) setDomain(cookie.getDomain());
            setMaxAge(cookie.getMaxAge());
            setPath(cookie.getPath());
            setSecure(cookie.getSecure());

            Object[] parsed = cookieFormat.parse(cookie.getValue());
            if (parsed != null) {
                this.username = (String)parsed[0];
                this.password = (String)parsed[1];
            }
        }
        catch (ParseException e) {
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
        return cookieFormat.format(new String[] { username, password });
    }
}
