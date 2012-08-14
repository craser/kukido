/*
 * URLMapFilter.java
 *
 * Created on September 11, 2004, 10:45 AM
 */

package net.kukido.servlet.filter;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import net.kukido.blog.log.Logging;

/**
 * 
 * @author craser
 */
public class UrlMapFilter implements Filter
{
    static public final String PATTERN_PARAM = "pattern";
    static public final String TEMPLATE_PARAM = "template";

    private Logger log;
    private List<UrlMapping> mappings;

    /** Creates a new instance of URLMapFilter */
    public UrlMapFilter()
    {
    }

    public void destroy()
    {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws java.io.IOException, javax.servlet.ServletException
    {
        log.info("Filtering.");
        String requestUri = ((HttpServletRequest) req).getRequestURI();

        log.debug("Searching " + mappings.size() + " mappings for match for " + requestUri);

        boolean found = false;
        for (int i = 0; i < mappings.size(); i++) {
            UrlMapping mapping = (UrlMapping) mappings.get(i);
            if (mapping.matches(requestUri)) {
                String newUri = mapping.getResource(requestUri);
                log.debug("found mapping for URI[" + requestUri + "]: " + newUri);
                RequestDispatcher dsp = req.getRequestDispatcher(newUri);
                dsp.forward(req, res);
                found = true;
                break;
            }
            else {
                log.debug("Mapping " + mapping + " does not match URI \"" + requestUri + "\"");
            }
        }

        if (!found) {
            log.info("No mapping found for URI[" + requestUri + "]");
            chain.doFilter(req, res);
        }
    }

    public void init(javax.servlet.FilterConfig config) throws javax.servlet.ServletException
    {
        log = Logging.getLogger(getClass());
        mappings = new ArrayList<UrlMapping>();
        for (int i = 1; config.getInitParameter(PATTERN_PARAM + i) != null; i++) {
            String pattern = config.getInitParameter(PATTERN_PARAM + i);
            String template = config.getInitParameter(TEMPLATE_PARAM + i);
            UrlMapping mapping = new UrlMapping(pattern, template);
            log.debug("adding: " + mapping);
            mappings.add(mapping);
        }

    }
}
