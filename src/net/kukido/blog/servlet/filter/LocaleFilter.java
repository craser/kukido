package net.kukido.blog.servlet.filter;

import net.kukido.blog.log.Logging;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by craser on 11/1/15.
 */
public class LocaleFilter implements Filter
{
    private Logger log;

    public void init(FilterConfig config) {
        log = Logging.getLogger(this.getClass());
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        Locale locale = Locale.US;
        log.info("Setting session locale to " + locale);
        ((HttpServletRequest)req).getSession().setAttribute("dmg.locale", locale);
        chain.doFilter(req, res);
    }

    public void destroy() {
    }
}
