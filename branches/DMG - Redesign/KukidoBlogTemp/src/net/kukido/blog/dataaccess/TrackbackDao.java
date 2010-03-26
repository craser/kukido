/*
 * CommentDAO.java
 *
 * Created on February 21, 2006, 9:24 PM
 */

package net.kukido.blog.dataaccess;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.text.MessageFormat;

import net.kukido.blog.datamodel.Trackback;
import net.kukido.blog.datamodel.LogEntry;
import net.kukido.blog.forms.*;
import net.kukido.blog.util.BucketMap;
import net.kukido.sql.*;
/**
 *
 * @author  craser
 */
public class TrackbackDao extends Dao
{
    // These are used to specify the direction of a Trackback via Trackback.setDirection(String)
    static public String DIRECTION_SENT = "sent";
    static public String DIRECTION_RECEIVED = "received";
    
    static private final String CREATE_SQL =
        "insert into TRACKBACKS ("
            + "Entry_ID"
            + ",Direction"
            + ",Is_Spam"
            + ",Date_Posted"
            + ",Url"
            + ",Title"
            + ",Excerpt"
            + ",Blog_Name"
            + ",IP_Address"
            + ",User_Agent"
            + ",Referrer"
            + ") values ("
            + ":Entry_ID"
            + ",:Direction"
            + ",:Is_Spam"
            + ",:Date_Posted"
            + ",:Url"
            + ",:Title"
            + ",:Excerpt"
            + ",:Blog_Name"
            + ",:IP_Address"
            + ",:User_Agent"
            + ",:Referrer"
            + ")";
    
    static private final String FIND_BY_TRACKBACK_ID_SQL =
        "select * from TRACKBACKS where Trackback_ID = :Trackback_ID order by Date_Posted asc";
    
    static private final String FIND_BY_ENTRY_ID_SQL = 
        "select * from TRACKBACKS where Entry_ID = :Entry_ID and Is_Spam != 'true' and Direction = 'received' order by Date_Posted asc";
    
    static private final String FIND_BY_ENTRY_IDS_SQL_FORMAT = 
        "select * from TRACKBACKS where Entry_ID in {0} and Is_Spam != ''true'' and Direction = ''received'' order by Date_Posted asc";
    
    static private final String FIND_SPAM =
        "select * from TRACKBACKS where Is_Spam = 'true' order by Date_Posted asc";
    
    static private final String DELETE_SQL = 
        "delete from TRACKBACKS where Trackback_ID = :Trackback_ID";
    
    static private final String FIND_RECENT_SQL = 
        "select * from TRACKBACKS where Is_Spam != 'true' order by Date_Posted desc limit :Num_Trackbacks";
    
    
    public void sendTrackbacks(LogEntry log)
        throws DataAccessException
    {
        Collection trackbacks = log.getTrackbacks();
        for (Iterator i = trackbacks.iterator(); i.hasNext(); ) 
        {
            try
            {
                Trackback t = (Trackback)i.next();
                t.setBlogName("DreadedMonkeyGod"); // TODO: Remove hard-coding
                t.setTitle(log.getTitle());
                t.setDirection(DIRECTION_SENT);
                t.setEntryId(log.getEntryId());
                URL url = buildTrackbackUrl(t);
                // This attempts to send the Trackback, does NOT CHECK FOR ERROR
                // CODES IN RESPONSE!
                InputStream in = url.openStream();
                byte[] buf = new byte[256];
                // Just read everything, and throw it away.
                System.out.println("RESPONSE FROM " + url);
                for (int read = in.read(buf); read > 0; read = in.read(buf)) {
                    System.out.write(buf, 0, read);
                }
                try { in.close(); } catch (Exception ignored) {}
                create(t); // And record this in my own DB as a "sent" trackback.
            }
            catch (Exception e)
            {
                // Log the exception, take no action.
                e.printStackTrace(System.err);
            }
        }
    }
    
    private URL buildTrackbackUrl(Trackback t) 
        throws MalformedURLException
    { 
        String[] params = new String[] {
          t.getUrl(),
          "http://www.dreadedmonkeygod.net/archives/" + t.getEntryId(), // TODO: Remove hard-coding.
          t.getTitle(),
          t.getBlogName()
        };
        
        MessageFormat f = new MessageFormat("{0}?url={1}&title={2}&blog_name={3}");
        
        // URL-Encode all of the parameters in the params array EXCEPT THE FIRST,
        // since the first (index 0) is the actual URL, and not a parameter
        // to be encoded.
        for (int i = 1; i < params.length; i++) {
            params[i] = URLEncoder.encode(params[i]);
        }
        
        String urlString = f.format(params);
        return new URL(urlString);
    }
    

    /**
     * Creates a new LogEntry from the information in the given entry.
     */
    public Trackback create(Trackback trackback)
	throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement create = null;
        try 
        {            
            conn = getConnection();
            create = new NamedParamStatement(conn, CREATE_SQL);
	    Timestamp now = new Timestamp(new java.util.Date().getTime());
            create.setTimestamp("Date_Posted", now);
            create.setInt("Entry_ID", trackback.getEntryId());
            create.setString("Direction", trackback.getDirection());
            create.setString("Is_Spam", Boolean.toString(trackback.isSpam()));
            create.setString("Url", trackback.getUrl());
            create.setString("Title", trackback.getTitle());
            create.setString("Excerpt", trackback.getExcerpt());
            create.setString("Blog_Name", trackback.getBlogName());
            create.setString("IP_Address", trackback.getIpAddress());
            create.setString("Referrer", trackback.getReferrer());
            create.setString("User_Agent", trackback.getUserAgent());
            create.executeUpdate();
            
            int commentId = getLastCreatedId(conn);
            return findByTrackbackId(commentId);
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
    
    public void delete(Trackback trackback)
        throws DataAccessException
    {
        deleteByTrackbackId(trackback.getTrackbackId());
    }
    
    public void deleteByTrackbackId(int trackbackId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement delete = null;
        try
        {
            conn = getConnection();
            delete = new NamedParamStatement(conn, DELETE_SQL);
            delete.setInt("Trackback_ID", trackbackId);
            delete.executeUpdate();
        }
        catch (Exception e)
        {
            try { conn.rollback(); } catch (Exception ignored) {}
            throw new DataAccessException("Unable to delete trackback with ID " + trackbackId, e);
        }
        finally
        {
            try { delete.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Trackback findByTrackbackId(int trackbackId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_TRACKBACK_ID_SQL);
            find.setInt("Trackback_ID", trackbackId);
            results = find.executeQuery();
            
            if (!results.next()) 
                throw new DataAccessException("No trackback found with ID " + trackbackId);
            
            return populateTrackback(results);
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Exception while finding trackback with ID " + trackbackId, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public Collection findByEntry(LogEntry entry)
        throws DataAccessException
    {
        return findByEntryId(entry.getEntryId());
    }
    
    /**
     * @return Collection of Trackback
     */
    public Collection findByEntryId(int entryId)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet results = null;
        try
        {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_ENTRY_ID_SQL);
            find.setInt("Entry_ID", entryId);
            results = find.executeQuery();
            
            List comments = new LinkedList();
            while (results.next()) {
                comments.add(populateTrackback(results));
            }
            
            return comments;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Exception while finding trackbacks for entry " + entryId, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    /**
     * @return Collection of Trackback
     */
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
            String sql = MessageFormat.format(FIND_BY_ENTRY_IDS_SQL_FORMAT, new String[] { buildParamList(entryIds.size()) });
            find = conn.prepareStatement(sql);
            int p = 1;
            for (Iterator i = entryIds.iterator(); i.hasNext(); p++) {
                int id = ((Integer)i.next()).intValue();
                find.setInt(p, id);
            }
            results = find.executeQuery();
            
            Map trackbacksById = new BucketMap();
            while (results.next()) {
                Trackback t = populateTrackback(results);
                Integer id = new Integer(t.getEntryId());
                trackbacksById.put(id, t);
            }
            
            return trackbacksById;
        }
        catch (SQLException e) 
        {
            throw new DataAccessException("Exception while finding trackbacks for entrys " + entryIds, e);
        }
        finally
        {
            try { results.close(); } catch (Exception ignored) {}
            try { find.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    /**
     * @return Collection of Trackback
     */
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
                comments.add(populateTrackback(results));
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
    

    
    public Collection findRecent(int numTrackbacks)
        throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement statement = null;
        ResultSet results = null;
        try {
            Collection entries = new ArrayList();
            conn = getConnection();
            statement = new NamedParamStatement(conn, FIND_RECENT_SQL);
            statement.setInt("Num_Trackbacks", numTrackbacks);
            results = statement.executeQuery();

            while (results.next()) {
                entries.add(populateTrackback(results));
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
        
    private Trackback populateTrackback(ResultSet results)
	throws SQLException
    {
        Trackback trackback = new Trackback();
        trackback.setTrackbackId(results.getInt("Trackback_ID"));
        trackback.setEntryId(results.getInt("Entry_ID"));
        trackback.setIsSpam(results.getBoolean("Is_Spam"));
        trackback.setDirection(results.getString("Direction"));
        trackback.setDatePosted(results.getTimestamp("Date_Posted"));
        trackback.setTitle(results.getString("Title"));
        trackback.setExcerpt(results.getString("Excerpt"));
        trackback.setUrl(results.getString("Url"));
        trackback.setBlogName(results.getString("Blog_Name"));
        trackback.setIpAddress(results.getString("IP_Address"));
        trackback.setUserAgent(results.getString("User_Agent"));
        trackback.setReferrer(results.getString("Referrer"));
        return trackback;
    }
}
