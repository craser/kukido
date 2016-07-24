package net.kukido.blog.log;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Serves as a wrapper to bind context lifecycle events to logging methods.
 *
 * Created by craser on 7/24/16.
 */
public class LoggingInitializer implements ServletContextListener
{
    private Logger log;

    public void contextInitialized(ServletContextEvent event) {
        try {
            Properties props = getConfigProperties(event.getServletContext());
            System.out.println("Initializing Log4J.");
            System.out.println("log4j.appender.FILE.File: " + props.getProperty("log4j.appender.FILE.File"));
            Logging.initialize(props);
            log = Logging.getLogger(this.getClass());
            log.info("Logging configured.");
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to initialize logging.", e);
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private Properties getConfigProperties(ServletContext context) throws IOException {
        InputStream fileIn = context.getResourceAsStream("WEB-INF/log4j.properties");
        Properties p = new Properties();
        p.load(fileIn);
        resolvePaths(p, context); // Converts paths in config file into real paths relative to the web app.

        return p;
    }

    private Properties resolvePaths(Properties p, ServletContext context) {
        // Path Resolution macro: path((path-to-whatever))
        Pattern pattern = Pattern.compile("path\\(\\((.*)\\)\\)");
        for (Enumeration e = p.propertyNames(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            String val = p.getProperty(key);
            Matcher matcher = pattern.matcher(val);
            if (matcher.matches()) {
                String tempPath = matcher.group(1);
                String realPath = context.getRealPath(tempPath); // Convert the path from the config file into a real path.
                p.setProperty(key, realPath);
            }
        }

        return p;
    }
}
