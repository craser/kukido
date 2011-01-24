/*
 * DataSourceFilter.java
 *
 * Created on March 4, 2006, 5:54 PM
 */

package net.kukido.blog.servlet.filter;

import net.kukido.blog.config.*;
import net.kukido.blog.security.*;
import net.kukido.blog.datamodel.*;
import net.kukido.blog.dataaccess.*;
import net.kukido.sql.*;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.*;

/**
 * 
 * @author craser
 */
public class DataSourceFilter implements Filter
{
    static private String jdbcDriver;// = "com.mysql.jdbc.Driver";
    static private String connectURL;// =
                                     // "jdbc:mysql://localhost/dreade2_web?user=dreade2_webuser&password=spiderman";

    static {
        DmgConfig conf = new DmgConfig();
        jdbcDriver = conf.getProperty("driver");
        connectURL = conf.getProperty("connecturl");
    }

    static public synchronized Connection getConnection()
            throws DataAccessException
    {
        try {
            Class.forName(jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(connectURL);

            return conn;
        }
        catch (Exception e) {
            throw new DataAccessException("Unable to create database connection: " + e, e);
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
