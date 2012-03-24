/*
 * DataSourceWrapper.java
 *
 * Created on March 4, 2006, 5:59 PM
 */

package net.kukido.sql;

import java.io.*;
import java.sql.*;
import javax.sql.*;

/**
 *
 * @author  craser
 */
public class ConnectionWrapper implements Connection
{
    private final Connection conn;
    
    public ConnectionWrapper(Connection conn)
    {
        this.conn = conn;
    }
    
    public Connection getConnection() 
    {
        return conn;
    }
    
    public void clearWarnings() throws java.sql.SQLException {
        conn.clearWarnings();
    }
    
    public void close() throws java.sql.SQLException {
        conn.close();
    }
    
    public void commit() throws java.sql.SQLException {
        conn.commit();
    }
    
    public java.sql.Statement createStatement() throws java.sql.SQLException {
        return conn.createStatement();
    }
    
    public java.sql.Statement createStatement(int param, int param1) throws java.sql.SQLException {
        return conn.createStatement(param, param1);
    }
    
    public java.sql.Statement createStatement(int param, int param1, int param2) throws java.sql.SQLException {
        return conn.createStatement(param, param1, param2);
    }
    
    public boolean getAutoCommit() throws java.sql.SQLException {
        return conn.getAutoCommit();
    }
    
    public String getCatalog() throws java.sql.SQLException {
        return conn.getCatalog();
    }
    
    public int getHoldability() throws java.sql.SQLException {
        return conn.getHoldability();
    }
    
    public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {
        return conn.getMetaData();
    }
    
    public int getTransactionIsolation() throws java.sql.SQLException {
        return conn.getTransactionIsolation();
    }
    
    public java.util.Map getTypeMap() throws java.sql.SQLException {
        return conn.getTypeMap();
    }
    
    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        return conn.getWarnings();
    }
    
    public boolean isClosed() throws java.sql.SQLException {
        return conn.isClosed();
    }
    
    public boolean isReadOnly() throws java.sql.SQLException {
        return conn.isReadOnly();
    }
    
    public String nativeSQL(String str) throws java.sql.SQLException {
        return conn.nativeSQL(str);
    }
    
    public java.sql.CallableStatement prepareCall(String str) throws java.sql.SQLException {
        return conn.prepareCall(str);
    }
    
    public java.sql.CallableStatement prepareCall(String str, int param, int param2) throws java.sql.SQLException {
        return conn.prepareCall(str, param, param2);
    }
    
    public java.sql.CallableStatement prepareCall(String str, int param, int param2, int param3) throws java.sql.SQLException {
        return conn.prepareCall(str, param, param2, param3);
    }
    
    public java.sql.PreparedStatement prepareStatement(String str) throws java.sql.SQLException {
        return conn.prepareStatement(str);
    }
    
    public java.sql.PreparedStatement prepareStatement(String str, String[] str1) throws java.sql.SQLException {
        return conn.prepareStatement(str, str1);
    }
    
    public java.sql.PreparedStatement prepareStatement(String str, int param) throws java.sql.SQLException {
        return conn.prepareStatement(str, param);
    }
    
    public java.sql.PreparedStatement prepareStatement(String str, int[] values) throws java.sql.SQLException {
        return conn.prepareStatement(str, values);
    }
    
    public java.sql.PreparedStatement prepareStatement(String str, int param, int param2) throws java.sql.SQLException {
        return prepareStatement(str, param, param2);
    }
    
    public java.sql.PreparedStatement prepareStatement(String str, int param, int param2, int param3) throws java.sql.SQLException {
        return conn.prepareStatement(str, param, param2, param3);
    }
    
    public void releaseSavepoint(java.sql.Savepoint savepoint) throws java.sql.SQLException {
        conn.releaseSavepoint(savepoint);
    }
    
    public void rollback() throws java.sql.SQLException {
        conn.rollback();
    }
    
    public void rollback(java.sql.Savepoint savepoint) throws java.sql.SQLException {
        conn.rollback(savepoint);
    }
    
    public void setAutoCommit(boolean param) throws java.sql.SQLException {
        conn.setAutoCommit(param);
    }
    
    public void setCatalog(String str) throws java.sql.SQLException {
        conn.setCatalog(str);
    }
    
    public void setHoldability(int param) throws java.sql.SQLException {
        conn.setHoldability(param);
    }
    
    public void setReadOnly(boolean param) throws java.sql.SQLException {
        conn.setReadOnly(param);
    }
    
    public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
        return conn.setSavepoint();
    }
    
    public java.sql.Savepoint setSavepoint(String str) throws java.sql.SQLException {
        return conn.setSavepoint(str);
    }
    
    public void setTransactionIsolation(int param) throws java.sql.SQLException {
        conn.setTransactionIsolation(param);
    }
    
    public void setTypeMap(java.util.Map map) throws java.sql.SQLException {
        conn.setTypeMap(map);
    }
    
}
