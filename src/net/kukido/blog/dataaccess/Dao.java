package net.kukido.blog.dataaccess;

import java.sql.*;
import net.kukido.blog.servlet.filter.*;
import net.kukido.sql.*;

/**
 * This class represents a generic Data Access Object, and provides
 * general functionality for access to persistant storage.
 * */

public abstract class Dao
{
    private static final String GET_LAST_CREATED_ID_SQL = 
        "select LAST_INSERT_ID()";
    
    public static Connection getConnection()
	throws DataAccessException
    {
	return DataSourceFilter.getConnection();
    }
    
    public int getLastCreatedId(final Connection conn)
        throws DataAccessException
    {
        NamedParamStatement findLastId = null;
        ResultSet lastIdResults = null;
        try
        {
            findLastId = new NamedParamStatement(conn, GET_LAST_CREATED_ID_SQL);
            lastIdResults = findLastId.executeQuery();
            
            if (!lastIdResults.next()) throw new DataAccessException("Unable to find most recently created id!");
            
            int lastId = lastIdResults.getInt(1);
            return lastId;            
        }
        catch (Exception e)
        {
            throw new DataAccessException("Unable to find most recently created id!", e);
        }
        finally
        {
            try { lastIdResults.close(); } catch (Exception ignored) {}
            try { findLastId.close(); } catch (Exception ignored) {}
        }
    }
    
    /**
     * @param numElements
     * @return A String of the form "(?,?,?,?...)"
     */
    public String buildParamList(int numElements)
    {
        StringBuffer b = new StringBuffer();
        
        b.append("(");
        for (int i = 0; i < numElements; i++) {
            b.append("?");
            if ((i + 1) < numElements) {
                b.append(",");
            }
        }
        b.append(")");
        return b.toString();
    }
}
