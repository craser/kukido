/*
 * CommentDAO.java
 *
 * Created on February 21, 2006, 9:24 PM
 */

package net.kukido.blog.dataaccess;

import java.sql.*;
import java.text.MessageFormat;
import javax.sql.*;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.blog.util.BucketMap;
import net.kukido.sql.*;
/**
 *
 * @author  craser
 */
public class CommentDao extends Dao
{
    
    static private final String CREATE_SQL =
        "insert into COMMENTS ("
        + " Entry_ID"
        + ",Is_Spam"
        + ",User_Name"
        + ",User_Email"
        + ",User_URL"
        + ",Comment"
        + ",User_Agent"
        + ",IP_Address"
        + ",Referrer"
        + ",Date_Posted"
        + ") values ("
        + ":Entry_ID"
        + ",:Is_Spam"
        + ",:User_Name"
        + ",:User_Email"
        + ",:User_URL"
        + ",:Comment"
        + ",:User_Agent"
        + ",:IP_Address"
        + ",:Referrer"
        + ",:Date_Posted"
        + ")";
    
    static private final String UPDATE_COMMENT_SQL =
        "update COMMENTS set"
        + " Is_Spam = :Is_Spam"
        + ",User_Name = :User_Name"
        + ",User_Email = :User_Email"
        + ",User_URL = :User_URL"
        + ",Comment = :Comment"
        + ",User_Agent = :User_Agent"
        + ",IP_Address = :IP_Address"
        + ",Referrer = :Referrer"
        + " where Comment_ID = :Comment_ID";
    
    static private final String FIND_BY_COMMENT_ID_SQL =
        "select * from COMMENTS where Comment_ID = :Comment_ID order by Date_Posted asc";
    
    static private final String FIND_COMMENTS_BY_ENTRY_ID_SQL = 
        "select * from COMMENTS where Entry_ID = :Entry_ID and Is_Spam != 'true' order by Date_Posted asc";
    
    static private final String FIND_COMMENTS_BY_ENTRY_IDS_SQL_FORMAT = 
        "select * from COMMENTS where Entry_ID in {0} and Is_Spam != 'true' order by Date_Posted asc";
    
    static private final String FIND_SPAM =
        "select * from COMMENTS where Is_Spam = 'true' order by Date_Posted asc";
    
    static private final String DELETE_SPAM_SQL =
        "delete from COMMENTS where Is_Spam = 'true'";
    
    static private final String DELETE_COMMENT_SQL = 
        "delete from COMMENTS where Comment_ID = :Comment_ID";
    
    static private final String DELETE_BY_ENTRY_ID_SQL = 
        "delete from COMMENTS where Entry_ID = :Entry_ID";
    
    static private final String FIND_RECENT_COMMENTS_SQL = 
        "select * from COMMENTS where Is_Spam != 'true' order by Date_Posted desc limit :Num_Comments";
    

    /**
     * Creates a new LogEntry from the information in the given entry. Also
     * verifies that the comment contains no HTML. This isn't the place I really
     * want that rule enforced, but it'll have to do until I figure out how the
     * spammers are getting past the Form bean validation.
     */
    public Comment create(Comment comment)
	throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement create = null;
        try 
        {
            LogDao logDao = new LogDao();
            LogEntry entry = logDao.findByEntryId(comment.getEntryId());
            if (!entry.getAllowComments()) 
                throw new DataAccessException("Blog posting \"" + entry.getTitle() + "\" is closed to comments.");
            
            if (!comment.getComment().equals(comment.getComment().replaceAll("<[^>]*>", "")))
                throw new DataAccessException("HTML not permitted in comments.");
            
            conn = getConnection();
            create = new NamedParamStatement(conn, CREATE_SQL);
	    Timestamp now = new Timestamp(new java.util.Date().getTime());
            create.setTimestamp("Date_Posted", now);
            create.setInt("Entry_ID", comment.getEntryId());
            create.setString("Is_Spam", Boolean.toString(comment.isSpam()));
            create.setString("User_Name", comment.getUserName());
            create.setString("User_Email", comment.getUserEmail());
            create.setString("User_URL", comment.getUserUrl());
            create.setString("Comment", comment.getComment());
            create.setString("IP_Address", comment.getIpAddress());
            create.setString("Referrer", comment.getReferrer());
            create.setString("User_Agent", comment.getUserAgent());
            create.executeUpdate();
            
            int commentId = getLastCreatedId(conn);
            return findByCommentId(commentId);
        }
        catch (SQLException e) 
        {
            try { conn.rollback(); } catch (Exception ignored) {}
            throw new DataAccessException("Caught while creating Log Entry", e);
        }
        finally {
            try { create.close(); } catch (Exception ignored) {}
            try { conn.close(); }   catch (Exception ignored) {}
        }
    }
    
    public void update(Comment comment)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement update = null;
        try
        {
            conn = getConnection();
            update = new NamedParamStatement(conn, UPDATE_COMMENT_SQL);
            update.setString("Is_Spam", Boolean.toString(comment.isSpam()));
            update.setString("User_Name", comment.getUserName());
            update.setString("User_Email", comment.getUserEmail());
            update.setString("User_URL", comment.getUserUrl());
            update.setString("Comment", comment.getComment());
            update.setInt("Comment_ID", comment.getCommentId());
            update.setString("IP_Address", comment.getIpAddress());
            update.setString("User_Agent", comment.getUserAgent());
            update.setString("Referrer", comment.getReferrer());
            update.executeUpdate();
        }
        catch (Exception e)
        {
            try { conn.rollback(); } catch (Exception ignored) {}
            throw new DataAccessException("Unable to update comment with ID " + comment.getCommentId(), e);
        }
        finally
        {
            try { update.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    
    
    public void deleteSpam()
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement delete = null;
        try
        {
            conn = getConnection();
            delete = new NamedParamStatement(conn, DELETE_SPAM_SQL);
            delete.executeUpdate();
        }
        catch (Exception e)
        {
            try { conn.rollback(); } catch (Exception ignored) {}
            throw new DataAccessException("Unable to delete all comment spam.", e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    public void delete(Comment comment)
        throws DataAccessException
    {
        deleteByCommentId(comment.getCommentId());
    }
    
    public void deleteByCommentId(int commentId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement delete = null;
        try
        {
            conn = getConnection();
            delete = new NamedParamStatement(conn, DELETE_COMMENT_SQL);
            delete.setInt("Comment_ID", commentId);
            delete.executeUpdate();
        }
        catch (Exception e)
        {
            try { conn.rollback(); } catch (Exception ignored) {}
            throw new DataAccessException("Unable to delete comment with ID " + commentId, e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public void deleteByEntryId(int entryId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement delete = null;
        try
        {
            conn = getConnection();
            delete = new NamedParamStatement(conn, DELETE_BY_ENTRY_ID_SQL);
            delete.setInt("Entry_ID", entryId);
            delete.executeUpdate();
        }
        catch (Exception e)
        {
            try { conn.rollback(); } catch (Exception ignored) {}
            throw new DataAccessException("Unable to delete comments with Entry_ID " + entryId, e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    
    
    public Comment findByCommentId(int commentId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_COMMENT_ID_SQL);
            find.setInt("Comment_ID", commentId);
            results = find.executeQuery();
            
            if (!results.next()) 
                throw new DataAccessException("No comment found with ID " + commentId);
            
            return populateComment(results);
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Exception while finding comment with ID " + commentId, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Collection findByEntryId(int entryId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_COMMENTS_BY_ENTRY_ID_SQL);
            find.setInt("Entry_ID", entryId);
            results = find.executeQuery();
            
            List comments = new LinkedList();
            while (results.next()) {
                comments.add(populateComment(results));
            }
            
            return comments;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Exception while finding comments for entry " + entryId, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Map findByEntryIds(Collection entryIds)
        throws DataAccessException
    {
        if (entryIds.isEmpty()) { return new BucketMap(); }
        
        Connection conn = null;
        PreparedStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            String sql = MessageFormat.format(FIND_COMMENTS_BY_ENTRY_IDS_SQL_FORMAT, new String[] { buildParamList(entryIds.size()) });
            find = conn.prepareStatement(sql);
            int p = 1;
            for (Iterator i = entryIds.iterator(); i.hasNext(); p++) {
                int id = ((Integer)i.next()).intValue();
                find.setInt(p, id);
            }
            results = find.executeQuery();
            
            Map m = new BucketMap();
            while (results.next()) {
                Comment c = populateComment(results);
                Integer id = new Integer(c.getEntryId());
                m.put(id, c);
            }
            
            return m;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Exception while finding comments for entrys " + entryIds, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Collection findSpam()
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_SPAM);
            results = find.executeQuery();
            
            List comments = new LinkedList();
            while (results.next()) {
                comments.add(populateComment(results));
            }
            
            return comments;
        }
        catch (SQLException e) {
            throw new DataAccessException("Exception while finding spam.", e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Collection find(CommentSelectionForm form)
        throws DataAccessException
    {
        List tables = new ArrayList();
	List criteria = new ArrayList();
        List fields = new ArrayList();
	Map params = new HashMap();

	if (form.getEntryId() >= 0)
	{
	    criteria.add("Entry_ID = :Entry_ID");
	    params.put("Entry_ID", new Integer(form.getEntryId()));
	}
        
        if (form.isSpam())
        {
            criteria.add("Is_Spam = :Is_Spam");
            params.put("Is_Spam", Boolean.toString(form.isSpam()));
        }
        
        if (form.getIpAddress() != null)
        {
            criteria.add("IP_Address = :IP_Address");
            params.put("IP_Address", form.getIpAddress());
        }
        
        if (form.getUserEmail() != null)
        {
            criteria.add("User_Email = :User_Email");
            params.put("User_Email", form.getUserEmail());
        }
        
        if (form.getYear() != -1)
        {
            criteria.add("year(Date_Posted) = :Year");
            params.put("Year", new Integer(form.getYear()));
        }
        
        if (form.getMonth() != -1)
        {
            criteria.add("month(Date_Posted) = :Month");
            params.put("Month", new Integer(form.getMonth()));
        }
        
        if (form.getDate() != -1)
        {
            criteria.add("dayofmonth(Date_Posted) = :Date");
            params.put("Date", new Integer(form.getDate()));
        }

	StringBuffer select = new StringBuffer("select COMMENTS.*");
        for (Iterator i = fields.iterator(); i.hasNext(); )
        {
            select.append(", ");
            select.append((String)i.next());
        }
        
        select.append(" from COMMENTS");
        
        for (Iterator i = tables.iterator(); i.hasNext(); )
        {
            select.append(", ");
            select.append((String)i.next());
        }

	if (!criteria.isEmpty()) select.append(" where ");

	for (Iterator i = criteria.iterator(); i.hasNext();)
	{
	    select.append((String)i.next());
            if (i.hasNext()) select.append(" and ");
	}
        
        select.append(" order by Date_Posted asc");
        
        if (form.getPageSize() > 0)
        {
            select.append(" limit :Start_Index,:Num_Entries");
            params.put("Num_Entries", new Integer(form.getPageSize()));
            params.put("Start_Index", new Integer((form.getPage() -1) * form.getPageSize()));
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
                Comment comment = populateComment(results);
                entries.add(comment);
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
    
    public Collection findRecentComments(int numComments)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement statement = null;
        ResultSet results = null;
        try {
            Collection entries = new ArrayList();
            conn = getConnection();
            statement = new NamedParamStatement(conn, FIND_RECENT_COMMENTS_SQL);
            statement.setInt("Num_Comments", numComments);
            results = statement.executeQuery();

            while (results.next()) {
                Comment comment = populateComment(results);
                entries.add(comment);
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
        
    private Comment populateComment(ResultSet results)
	throws SQLException
    {
        Comment comment = new Comment();
        comment.setCommentId(results.getInt("Comment_ID"));
        comment.setEntryId(results.getInt("Entry_ID"));
        comment.setDatePosted(results.getTimestamp("Date_Posted"));
        comment.setUserName(results.getString("User_Name"));
        comment.setUserUrl(results.getString("User_URL"));
        comment.setUserEmail(results.getString("User_Email"));
        comment.setIsSpam(results.getBoolean("Is_Spam"));
        comment.setComment(results.getString("Comment"));
        comment.setUserAgent(results.getString("User_Agent"));
        comment.setIpAddress(results.getString("IP_Address"));
        comment.setReferrer(results.getString("Referrer"));
        
        return comment;
    }
}
