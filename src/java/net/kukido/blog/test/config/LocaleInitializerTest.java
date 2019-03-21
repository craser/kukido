package net.kukido.blog.test.config;

import net.kukido.blog.config.LocaleInitializer;
import org.junit.Test;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.Locale;

/**
 * Created by craser on 8/21/16.
 */
public class LocaleInitializerTest
{
    @Test
    public void test_sessionCreated() {
        HttpSession session = mock(HttpSession.class);
        HttpSessionEvent event = mock(HttpSessionEvent.class);
        when(event.getSession()).thenReturn(session);

        LocaleInitializer localeInitializer = new LocaleInitializer();
        localeInitializer.sessionCreated(event);

        verify(session).setAttribute(eq("dmg.locale"), eq(Locale.US));
    }

}
