/*
 * DataSourceFilter.java
 *
 * Created on March 4, 2006, 5:54 PM
 */

package net.kukido.blog.servlet.filter;

import net.kukido.blog.dataaccess.*;

import java.io.*;
import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.sql.*;

/**
 * 
 * @author craser
 */
public class DataSourceFilter implements Filter
{
    
    static public synchronized Connection getConnection()
        throws DataAccessException
    {
        try {
            Context ctx = (Context)new InitialContext().lookup("java:comp/env");
            DataSource source = (DataSource)ctx.lookup("jdbc/dreade2_web");
            Connection conn = source.getConnection();
            
            return conn;
        }
        catch (Exception e) {
            throw new DataAccessException("Error trying open database connection.", e);
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException
    {
        try {
            // System.out.println("OPEN CONNECTIONS BEFORE doFilter: " +
            // numConnections);
            chain.doFilter(req, res);
        }
        catch (Exception ignored) {
            ignored.printStackTrace(System.err);
            throw new ServletException(ignored);
        }
    }

    /**
     * No-op.
     **/
    public void init(FilterConfig config)
    {
    }

    /**
     * No-op.
     **/
    public void destroy()
    {
    }

}
