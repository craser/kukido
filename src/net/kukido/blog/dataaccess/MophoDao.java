/*
 * MophoDao.java
 *
 * Created on March 26, 2004, 8:39 PM
 */

package net.kukido.blog.dataaccess;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import net.kukido.sql.*;
import net.kukido.blog.datamodel.MophoEntry;
import net.kukido.blog.forms.*;

/**
 *
 * @author  craser
 */
public class MophoDao extends net.kukido.blog.dataaccess.Dao 
{
    static private final String CREATE_SQL =
	"insert into MOPHO_ENTRIES "
	+ "(Date_Posted, User_ID, User_Name, Title, File_Name, Bytes) "
	+ "values "		
	+ "(:Date_Posted"	// 1  Date_Posted
	+ ",:User_ID"		// 2  User_ID
	+ ",:User_Name"		// 3  User_Name
	+ ",:Title"		// 4  Title
	+ ",:File_Name"	        // 5  File_Name
	+ ",:Bytes"		// 6  Bytes: actual image bytes.
	+ ")";			

    static private final String UPDATE_SQL =
	"update MOPHO_ENTRIES "
	+ "set Date_Posted = :Date_Posted"       // 1  Date_Posted
	+ ",User_ID = :User_ID"	                 // 2  User_ID
	+ ",User_Name = :User_Name"              // 3  User_Name
	+ ",Title = :Title"	                 // 4  Title
	+ ",File_Name = :File_Name "             // 5  Image_File_Name
	+ ",Bytes = :Bytes"	                 // 6 Bytes
	+ " where " 
	+ " Entry_ID = :Entry_ID";               // 7 Entry_ID

    static private final String RETRIEVE_LATEST_SQL =
	"select * from MOPHO_ENTRIES order by Entry_ID desc LIMIT 1";
    
    static private final String FIND_BY_FILE_NAME_SQL =
	"select * from MOPHO_ENTRIES where File_Name = :File_Name";

    static private final String RETRIEVE_ALL_ENTRIES_SQL =
        "select * from MOPHO_ENTRIES order by Entry_ID desc";


    public void create(MophoEntry mophoEntry)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement create = null;
        try {
            conn = getConnection();
            create = new NamedParamStatement(conn, CREATE_SQL);
            create.setDate("Date_Posted", new java.sql.Date(new java.util.Date().getTime()));
            create.setInt("User_ID", mophoEntry.getUserId());
            create.setString("User_Name", mophoEntry.getUserName());
            create.setString("Title", mophoEntry.getTitle());
            create.setString("File_Name",  mophoEntry.getFileName());
	    create.setBytes("Bytes", mophoEntry.getBytes());
            create.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Caught while creating Log Entry", e);
        }
        finally {
            try { create.close(); } catch (Exception ignored) {}
            try { conn.close(); }   catch (Exception ignored) {}
        }
    }
	
    public void update(MophoEntry mophoEntry)
	throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement update = null;
        try {
            conn = getConnection();
            update = new NamedParamStatement(conn, UPDATE_SQL);
            update.setDate("Date_Posted", new java.sql.Date(mophoEntry.getDatePosted().getTime()));
            update.setInt("User_ID", mophoEntry.getUserId());
            update.setString("User_Name", mophoEntry.getUserName());
            update.setString("Title", mophoEntry.getTitle());
            update.setString("File_Name", mophoEntry.getFileName());
	    update.setBytes("Bytes", mophoEntry.getBytes());
            update.setInt("Entry_ID", mophoEntry.getEntryId());
            update.executeUpdate();
        }
        catch (SQLException e) {
	    e.printStackTrace(System.out);
            throw new DataAccessException("Unable to update Mopho Entry #" + mophoEntry.getEntryId(), e);
        }
        finally {
            try { update.close(); } catch (Exception ignored) {}
            try { conn.close(); }   catch (Exception ignored) {}
        }
    }
    
    public MophoEntry findLatest()
	throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement findByEntry = null;
        ResultSet results = null;
        try {
            conn = getConnection();
            findByEntry = new NamedParamStatement(conn, RETRIEVE_LATEST_SQL);
            results = findByEntry.executeQuery();
            if (!results.next())
                throw new DataAccessException("No mobile photo log entry found.");
            MophoEntry entry = populateEntry(results);
            return entry;
        }
        catch (SQLException e) {
            throw new DataAccessException("Caught while retrieving latest Mopho", e);
        }
        finally {
            try { results.close(); } catch (Exception ignored) {}
            try { findByEntry.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }    
    
    
    public Collection findAllEntries()
	throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement findAll = null;
        ResultSet results = null;
        try {
            Collection entries = new ArrayList();
            conn = getConnection();
            findAll = new NamedParamStatement(conn, RETRIEVE_ALL_ENTRIES_SQL);
            results = findAll.executeQuery();
            while (results.next()) {
                MophoEntry entry = populateEntry(results);
                entries.add(entry);
            }
            
            return entries;
        }
        catch (SQLException e) {
            throw new DataAccessException("Caught while retrieving all Mopho entries", e);
        }
        finally {
            try { results.close(); } catch (Exception ignored) {}
            try { findAll.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }

    public MophoEntry findByFileName(String fileName)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement find = null;
	ResultSet results = null;
	try
	{
	    conn = getConnection();
	    find = new NamedParamStatement(conn, FIND_BY_FILE_NAME_SQL);
	    find.setString("File_Name", fileName);
	    results = find.executeQuery();
	    if (!results.next()) throw new DataAccessException("No records found for file name " + fileName);
	    return populateEntry(results);
	}
	catch (SQLException e)
	{
	    throw new DataAccessException("Could not retrieve Mopho entry for file name " + fileName, e);
	}
	finally
	{
	    try { results.close(); } catch (Exception ignored) {}
	    try { find.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }
	

    public Collection find(MophoSelectionForm form)
        throws DataAccessException
    {
	List criteria = new ArrayList();
	Map params = new HashMap();

	if (form.getEntryId() >= 0)
	{
	    criteria.add("Entry_ID = :Entry_ID");
	    params.put("Entry_ID", new Integer(form.getEntryId()));
	}
	else
	{
	    if (form.getUserId() >= 0)
	    {
		criteria.add("User_ID = :User_ID");
		params.put("User_ID", new Integer(form.getUserId()));
	    }
	}

	StringBuffer select = new StringBuffer("select * from MOPHO_ENTRIES ");

	if (criteria.size() > 0) select.append(" where ");

	for (Iterator i = criteria.iterator(); i.hasNext();)
	{
	    select.append((String)i.next());
            if (i.hasNext()) select.append(" and ");
	}
        
        select.append(" order by Entry_ID desc");
        
        if (form.getPageSize() > 0)
        {
            select.append(" limit :Start_Index,:Num_Entries");
            params.put("Num_Entries", new Integer(form.getPageSize()));
            params.put("Start_Index", new Integer(form.getPage() * form.getPageSize()));
        }

	return find(select.toString(), params);
    }

    private Collection find(String select, Map params)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement statement = null;
        ResultSet results = null;
        try {
            Collection entries = new ArrayList();
            conn = getConnection();
            statement = new NamedParamStatement(conn, select);
            statement.bindAll(params);
            results = statement.executeQuery();

            while (results.next()) {
                MophoEntry entry = populateEntry(results);
                entries.add(entry);
            }
            
            return entries;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Caught while executing query:", e);
        }
        finally 
        {
            try { results.close(); } catch (Exception ignored) {}
            try { statement.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }	


    /**
     * Creates a new MophoEntry and populates it with the data from the
     * current row of given ResultSet.
     */
    private MophoEntry populateEntry(ResultSet results)
	throws SQLException
    {
        MophoEntry entry = new MophoEntry();
        entry.setEntryId(results.getInt("Entry_ID"));
        entry.setDatePosted(results.getDate("Date_Posted"));
        entry.setUserId(results.getInt("User_ID"));
        entry.setUserName(results.getString("User_Name"));
        entry.setTitle(results.getString("Title"));
        entry.setFileName(results.getString("File_Name"));
	entry.setBytes(results.getBytes("Bytes"));
        return entry;
    }
    
    public MophoEntry getLatest()
	throws DataAccessException
    {
        return findLatest();
    }
}
