package net.kukido.blog.test.security;

import net.kukido.blog.security.LoginCookie;
import javax.servlet.http.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginCookieTest
{
    /*
    public LoginCookie(Cookie cookie)
    {
        try {
            super(cookie.getName(), cookie.getValue());
            setComment(cookie.getComment());
            if (cookie.getDomain() != null) setDomain(cookie.getDomain());
            setMaxAge(cookie.getMaxAge());
            setPath(cookie.getPath());
            setSecure(cookie.getSecure());

            Object[] parsed = cookieFormat.parse(cookie);
            if (parsed != null) {
                this.username = parsed[0];
                this.password = parsed[1];
            }
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Cookie not a correctly formatted login cookie!");
        }
    }
    */

	@Test
	public void test_copyConstructor() {
		try {
            // Dummy data to use for testing.
            final String USER = "USERNAME@WHATEVER.COM";
            final String PASS = "P0T3NT1@LLY-B*GUS%PASS#;!";
            final String DOMAIN = "DUMMY.DOMAIN.WORLD";
            final String COMMENT = "DUMMY COMMENT";
            final int MAX_AGE = 1138;
            final String PATH = "DUMMY PATH";
            final boolean SECURE = true;
            
            Cookie cookie = new LoginCookie(USER, PASS);
            cookie.setComment(COMMENT);
            cookie.setDomain(DOMAIN);
            cookie.setMaxAge(MAX_AGE);
            cookie.setPath(PATH);
            cookie.setSecure(SECURE);

            LoginCookie login = new LoginCookie(cookie); // Should copy everything over correctly.
            assertEquals(cookie.getComment(), login.getComment());
            assertEquals(cookie.getDomain(), login.getDomain());
            assertEquals(cookie.getMaxAge(), login.getMaxAge());
            assertEquals(cookie.getPath(), login.getPath());
            assertEquals(cookie.getSecure(), login.getSecure());

		}
		catch (Exception e) {
			fail(e.toString());
		}
	}

}
