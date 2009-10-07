package net.kukido.blog.dataaccess;

import java.sql.*;
import javax.sql.*;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.sql.*;

/**
 * Implements CRUD design pattern: Create, Retrieve, Update, Delete.
 * */
public class UserDao extends Dao
{
    static private final String CREATE_SQL =
	"insert into USERS "
	+ "(User_Name, Password, Email, Url) "
	+ "values "
	+ "(:User_Name"			// 1  User_Name
	+ ",password(:Passsword)"	// 2  Password
        + ",:Email"                     // 3  Email
        + ",:Url";                      // 4  Url

    static private final String RETRIEVE_BY_USERID_SQL =
	"select * from USERS where User_ID = ?"; // 1 User_ID

    private static final String RETRIEVE_BY_USER_NAME_PASSWORD_SQL =
	"select * from USERS where User_Name = :User_Name " // 1 User_Name
	+ "and Password = password(:Password)"; // 2 Password

    static private final String UPDATE_SQL =
	"update USERS set "
	+ "Password = password(:New_Password) "     // 1  New Password
        + ",Email = :Email "                        // 2  Email
        + ",Url = :Url "                            // 3  Url
	+ " where User_Name = :User_Name "          // 4  User_ID
        + " and Password = password(:Password)";    // 5 Old Password


    private static final String DELETE_SQL =
	"delete from USERS where User_ID = :User_ID"; // 1 User_ID

    /**
     * Creates a new User from the information in the given User object,
     * */
    public User create(User user)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement create = null;
	try
	{
	    conn = getConnection();
	    create = new NamedParamStatement(conn, CREATE_SQL);
	    create.setString("User_Name", user.getUserName());
	    create.setString("Password", user.getPassword());
            create.setString("Email", user.getEmail());
            create.setString("Url", user.getUrl());
	    create.executeUpdate();
	    return findByUserNamePassword(user.getUserName(), user.getPassword());
	}
	catch (SQLException e)
	{
	    throw new DataAccessException("Caught while creating new User", e);
	}
	finally
	{
	    try { create.close(); } catch (Exception ignored) {}
	    try { conn.close(); }   catch (Exception ignored) {}
	}
    }
    
    /**
     * Updates user data to be congruent with the given User bean.
     *
     * @param user The new user data, including desired password.  DO NOT LEAVE PASSWORD BLANK!
     * @param password The current password for this user.
     */
    public User update(User user, String password)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement update = null;
        try
        {
            conn = getConnection();
            update = new NamedParamStatement(conn, UPDATE_SQL);
            update.setString("New_Password", user.getPassword());
            update.setString("Email", user.getEmail());
            update.setString("Url", user.getUrl());
            update.setString("User_Name", user.getUserName());
            update.setString("Password", password);
            update.executeUpdate();
            return findByUserNamePassword(user.getUserName(), user.getPassword());
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Caught while trying to update user " + user.getUserName(), e);
        }
        finally
        {
            try { update.close(); } catch (Exception ignored) {}
            try { conn.close(); }   catch (Exception ignored) {}
        }
    }

    public User findByUserId(int userId)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement findByUserId = null;
	ResultSet results = null;
	try	    
	{
	    conn = getConnection();
	    findByUserId = new NamedParamStatement(conn, RETRIEVE_BY_USERID_SQL);
	    findByUserId.setInt("User_ID", userId);
	    results = findByUserId.executeQuery();
	    if (!results.next())
		throw new DataAccessException("No user found with User_ID " + userId);
	    User user = populateUser(results);
	    return user;
	}
	catch (SQLException e)
	{
	    throw new DataAccessException("Caught while retrieving user with User_ID " + userId, e);
	}
	finally
	{
	    try { results.close(); } catch (Exception ignored) {}
	    try { findByUserId.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }


    public User findByUserNamePassword(String userName, String password)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement findByNamePass = null;
	ResultSet results = null;
	try	    
	{
	    conn = getConnection();
	    findByNamePass = new NamedParamStatement(conn, RETRIEVE_BY_USER_NAME_PASSWORD_SQL);
	    findByNamePass.setString("User_Name", userName);
	    findByNamePass.setString("Password", password);
	    results = findByNamePass.executeQuery();

	    if (!results.next())
		throw new DataAccessException("Unrecognised username or incorrect password.");

	    User user = populateUser(results);
	    return user;
	}
	catch (SQLException e)
	{
	    throw new DataAccessException(e.getMessage(), e);
	}
	finally
	{
	    try { results.close(); } catch (Exception ignored) {}
	    try { findByNamePass.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }
    
    private User populateUser(ResultSet rs)
        throws SQLException
    {
        User user = new User();
	user.setUserName(rs.getString("User_Name"));
        user.setUserId(rs.getInt("User_ID"));
        user.setEmail(rs.getString("Email"));
        user.setUrl(rs.getString("Url"));
        return user;
    }
}
