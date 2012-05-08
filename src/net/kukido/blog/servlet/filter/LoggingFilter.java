package net.kukido.blog.servlet.filter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;

import net.kukido.blog.log.Logging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * I'm implementing logging initialization as a filter because:
 *   1. I'm averse to having certain "magic" servlets configured to load on startup.
 *   2. This lets me configure logging using soft-coded paths.
 *   3. This lets me report errors in logging initialization up to the ExceptionHandlerFilter.
 *   
 *   This MIGHT mean that logging done by code run BEFORE this filter will be lost, dumped
 *   in weird places, cause errors, etc.  BEWARE.
 *   
 *   Paths in the config file will be converted into "real" paths.  Paths are denoted by:
 *   
 *       foo.bar.path=path((path-to-resource))
 *   
 * @author craser
 *
 */
public class LoggingFilter implements Filter
{
    private Logger log;
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        chain.doFilter(req, res);
    }

    public void init(FilterConfig config) throws ServletException
    {
        System.out.println("FOO");
        Properties props = getConfigProperties(config);
        PropertyConfigurator.configure(props);
        log = Logging.getLogger(this.getClass());
        log.info("Logging configured.");
    }
    
    private Properties getConfigProperties(FilterConfig config)
            throws ServletException
    {
        FileReader fileIn = null;
        try {
            String configFileParam = config.getInitParameter("configFile");
            String configFileName = config.getServletContext().getRealPath(configFileParam);
            fileIn = new FileReader(configFileName);
            
            Properties p = new Properties();
            p.load(fileIn);  // I am an idiot.
            resolvePaths(p, config); // Converts paths in config file into real paths relativ to the web app.
            
            return p;
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    private Properties resolvePaths(Properties p, FilterConfig config) {
        // Path Resolution macro: path((path-to-whatever))
        Pattern pattern = Pattern.compile("path\\(\\((.*)\\)\\)");
        for (Enumeration e = p.propertyNames(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            String val = p.getProperty(key);
            Matcher matcher = pattern.matcher(val);
            if (matcher.matches()) {
                String tempPath = matcher.group(1);
                String realPath = config.getServletContext().getRealPath(tempPath); // Convert the path from the config file into a real path.
                p.setProperty(key, realPath);
            }
        }
        
        return p;
    }

    public void destroy()
    {
        // TODO Auto-generated method stub
        
    }
}
