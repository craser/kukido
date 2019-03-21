package net.kukido.blog.config;


import com.sun.tools.example.debug.bdi.SessionListener;
import net.kukido.blog.log.Logging;
import org.apache.log4j.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.EventObject;
import java.util.Locale;

/**
 * Creates the default locale in the Session.
 *
 * Created by craser on 8/21/16.
 */
public class LocaleInitializer implements HttpSessionListener
{
    private Logger log;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        try {
            log = Logging.getLogger(getClass());
            Locale locale = Locale.US;
            log.info("Setting dmg.locale to " + locale);
            event.getSession().setAttribute("dmg.locale", locale);
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to initialize locale.", e);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }

}
