package net.kukido.servlet.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.kukido.blog.log.Logging;

import org.apache.log4j.Logger;

/**
 * RootCacheFilter is a hack to get around limitations in Tomcat and Resin.
 * For some reason, trying to map a filter to ONLY the root page of an app
 * is problematic on these platforms.  So, I've created a filter that is 
 * identical to CacheFilter, but only takes any action if the request URI
 * is empty, indicating that the browser has requested the root page of 
 * the app.
 * 
 * Hacky, but effective.
 * 
 * @author craser
 *
 */
public class RootCacheFilter extends CacheFilter 
{
	private Logger log;	
	private Pattern pattern;
	
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        String requestUri = ((HttpServletRequest) request).getRequestURI();
    	log.info("RootCacheFilter: \"" + requestUri + "\"");
    	log.debug("RootCacheFilter: regex: " + pattern.toString());
    	if (pattern.matcher(requestUri).matches()) {
    		log.info("RootCacheFilter: passing to superclass");
    		super.doFilter(request,  response, chain);
    	}
    	else {
    		log.info("RootCacheFilter: bypassing cache, passing down the chain.");
    		chain.doFilter(request, response);
    	}
    }
    
    public void init(FilterConfig conf) throws ServletException
    {   
    	super.init(conf);
        log = Logging.getLogger(getClass().getName() + "." + conf.getFilterName());
        pattern = buildRootMatcher(conf);
    }
    
    private Pattern buildRootMatcher(FilterConfig conf) 
    {
    	log.debug("Building regex to match only the root of the app.");
        // Build a regex to match only the root 
        String ctxPath = conf.getServletContext().getServletContextName();
        log.debug("Context path: " + ctxPath);
        String ctxCheck = Pattern.quote(ctxPath);
        String startSlashCheck = ctxPath.startsWith("/") ? "" : "/";
        String endSlashCheck = ctxPath.endsWith("/") ? "?" : "/?";
        String regex =  startSlashCheck + ctxCheck + endSlashCheck + "$";
        log.debug("regex: " + regex);
        return Pattern.compile(regex);
    }

}
