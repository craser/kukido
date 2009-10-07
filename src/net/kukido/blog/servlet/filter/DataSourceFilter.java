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
 * @author  craser
 */
public class DataSourceFilter implements Filter
{
    static private ThreadLocal connectionLocal;    
    static private ThreadLocal nestingLocal;
    static private ThreadLocal nameLocal;
    static int nameIndex = 0;
    static int numConnections = 0;
    static private String jdbcDriver;// = "com.mysql.jdbc.Driver";
    static private String connectURL;// = "jdbc:mysql://localhost/dreade2_web?user=dreade2_webuser&password=spiderman";
    

    static 
    {
        DmgConfig conf = new DmgConfig();
        jdbcDriver = conf.getProperty("driver");
        connectURL = conf.getProperty("connecturl");
        connectionLocal = new ThreadLocal();
        nestingLocal = new ThreadLocal();
        nameLocal = new ThreadLocal();
    }

    
    static public synchronized Connection getConnection()
        throws DataAccessException
    {
        if (connectionLocal.get() == null) createConnection();
        Connection conn = (Connection)connectionLocal.get();
        Connection wrapper = new ConnectionWrapper(conn) {
            public void close() throws SQLException { commit(); }
        };
        return wrapper;
    }
    
    static private synchronized int getLevel()
    {
        Integer level = (Integer)nestingLocal.get();
        return (level == null) ? 0 : level.intValue();
    }
    
    static private synchronized void push()
    {
        int level = getLevel();
        nestingLocal.set(new Integer(level + 1));        
    }
    
    static private synchronized void pop()
    {
        int level = getLevel();
        nestingLocal.set(new Integer(level - 1));
    }
    
    static private synchronized void createConnection()
        throws DataAccessException
    {
        try
        {
            Class.forName(jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(connectURL);
            nameLocal.set(Integer.toString(nameIndex++));
            connectionLocal.set(conn);
            numConnections++;
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to create database connection: " + e, e);
        }
    }
    
    static private synchronized void destroyConnection()
    {
        try
        {
            Connection conn = (Connection)connectionLocal.get();
            String name = (String)nameLocal.get();
            if (conn != null)
            {
                //System.out.println("Closing open connection " + name);
                conn.close();
                numConnections--;
                connectionLocal.set(null); // Salt the earth behind us...
                //nameLocal.set(null); Leave a name, so that we can track Null Pointers.
            }
            else
            {
                //System.out.println("Not destroying connection " + name);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
        finally
        {
            connectionLocal.set(null);
        }
    }
    
    
    
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException
    {
	HttpServletRequest httpReq = (HttpServletRequest)req;
	try
        {
            push();
            //System.out.println("OPEN CONNECTIONS BEFORE doFilter: " + numConnections);
            chain.doFilter(req,res);
	}
	catch (Exception ignored)
	{
            ignored.printStackTrace(System.err);
            throw new ServletException(ignored);
	}
	finally
	{
            pop();
            if (getLevel() <= 0) {
                //System.out.println("Found level " + getLevel() + ", closing connection.");
                destroyConnection(); 
                //System.out.println("OPEN CONNECTIONS AFTER doFilter:  " + numConnections);
            }
	}
    }
    
    /**
     * No-op.
     **/
    public void init(FilterConfig config) {}

    /**
     * No-op.
     **/
    public void destroy() {}
    
}
