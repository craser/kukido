/*
 * DataSourceFilter.java
 *
 * Created on March 4, 2006, 5:54 PM
 */

package net.kukido.blog.servlet.filter;

import net.kukido.blog.config.DmgConfig;
import net.kukido.blog.dataaccess.*;
import net.kukido.sql.ConnectionWrapper;

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
	// Old-school backup, so that I can test locally.
    static private String jdbcDriver;
    static private String connectURL;
    static {
        DmgConfig conf = new DmgConfig();
        jdbcDriver = conf.getProperty("driver");
        connectURL = conf.getProperty("connecturl");
        System.out.println("################################################################################");
        System.out.println("connectURL: " + connectURL);
    }
    
    // New stuff
    static private ThreadLocal<ConnectionWrapper> threadConn = new ThreadLocal<ConnectionWrapper>();
    
    static public Connection getConnection() throws DataAccessException
    {
        if (threadConn.get() != null) {
            //System.out.println("Getting ThreadLocal Connection");
            return threadConn.get();
        }
        else {
            //System.out.println("Getting REAL connection.");
            Connection conn = getConnectionReal();
            ConnectionWrapper wrapped = new ConnectionWrapper(conn) {
                public void close() {
                    //System.out.println("NOT closing connection.");
                    // Do nothing.  The connection is closed after the response is complete.
                }
            };
            
            //System.out.println("Saving connection in threadConn");
            threadConn.set(wrapped);
            return wrapped;
        }
    }
    
    static public synchronized Connection getConnectionReal()
        throws DataAccessException
    {
        try {
            Context ctx = (Context)new InitialContext().lookup("java:comp/env");
            DataSource source = (DataSource)ctx.lookup("jdbc/dreade2_web");
            Connection conn = source.getConnection();
            
            return conn;
        }
        catch (Exception e) {
            //System.out.println("Error trying open database connection.  Failing back to Old School method.");
            return getConnectionOldSchool();
        }
    }

    static public synchronized Connection getConnectionOldSchool()
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
            // //System.out.println("OPEN CONNECTIONS BEFORE doFilter: " +
            // numConnections);
            chain.doFilter(req, res);
        }
        catch (Exception ignored) {
            ignored.printStackTrace(System.err);
            throw new ServletException(ignored);
        }
        finally {
            //System.out.println("Deleting connection");
            try {
                if (threadConn.get() != null) {
                    Connection conn = threadConn.get().getConnection();
                    conn.close();
                }
            }
            catch (Exception e) {
                System.err.println("Failed attempt to close database connection:");
                e.printStackTrace(System.err);
            }
            finally {
                threadConn.set(null);
            }
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
