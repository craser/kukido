package net.kukido.blog.dataaccess;

import java.sql.*;
import javax.sql.*;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.sql.*;

public class LinkDao extends Dao implements Iterator
{
    private Iterator iterator;

    static private final String CREATE_SQL =
	"insert into LINKS"
	+ " (Date_Posted, Title, Text, Url)"
	+ " values ("
	+ ":Date_Posted"	// Date Posted (1)
	+ ",:Title"		// Title       (2)
	+ ",:Text"		// Text        (3)
	+ ",:Url"		// URL         (4)
	+ ")";

    static private final String SELECT_SQL =
	"select * from LINKS order by Link_ID desc Limit :Num_Links";


    static private final String UPDATE_SQL =
	"update LINKS set"
	+ " Date_Posted = :Date_Posted"	// Date_Posted (1)
	+ ",Title = :Title"	        // Title       (2)
	+ ",Text = :Text"               // Text        (3)
	+ ",Url = :Url"                 // Url         (4)
	+ " where"
	+ "Link_ID = :Link_ID";         // Link_ID     (5)
	
    static private final String DELETE_SQL =
	"delete from LINKS where Link_ID = :Link_ID";



    public void create(LogEntry entry)
	throws DataAccessException
    {
	Link link = new Link();
	link.setTitle(entry.getViaText()); // Text/Title Reversal intentional
	link.setText(entry.getViaTitle());
	link.setUrl(entry.getViaUrl());
	create(link);
    }
	

    public void create(Link link)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement create = null;

	try
	{
	    conn = getConnection();
	    create = new NamedParamStatement(conn, CREATE_SQL);

	    create.setTimestamp("Date_Posted", new java.sql.Timestamp(new java.util.Date().getTime()));
	    create.setString("Title", link.getTitle());
	    create.setString("Text", link.getText());
	    create.setString("Url", link.getUrl());

	    create.executeUpdate();
	}
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to create new Link: ", e);
	}
	finally
	{
	    try { create.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }

    public Collection find(int numLinks)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement find = null;
	ResultSet results = null;

	try
	{
	    conn = getConnection();
	    find = new NamedParamStatement(conn, SELECT_SQL);

	    find.setInt("Num_Links", numLinks);
	    results = find.executeQuery();

	    ArrayList links = new ArrayList();
	    while (results.next())
	    {
		links.add(populateLink(results));
	    }
	    return links;
	}
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to retrieve " + numLinks + " records from LINKS", e);
	}
	finally
	{
	    try { results.close(); } catch (Exception ignored) {}
	    try { find.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }

    public void update(Link link)
	throws DataAccessException
    {
	Connection conn = null;
	NamedParamStatement update = null;

	try
	{
	    conn = getConnection();
	    update = new NamedParamStatement(conn, UPDATE_SQL);
	    update.setTimestamp("Date_Posted", new java.sql.Timestamp(link.getDatePosted().getTime()));
	    update.setString("Title", link.getTitle());
	    update.setString("Text", link.getText());
	    update.setString("Url", link.getUrl());
	    update.setInt("Link_ID", link.getLinkId());
	    update.executeUpdate();
	}
	catch (Exception e)
	{
	    throw new DataAccessException("Unable to update link with Link_ID = " + link.getLinkId(), e);
	}
	finally
	{
	    try { update.close(); } catch (Exception ignored) {}
	    try { conn.close(); } catch (Exception ignored) {}
	}
    }

    private Link populateLink(ResultSet results)
	throws SQLException
    {
	Link link = new Link();
	link.setLinkId(results.getInt("Link_ID"));
	link.setDatePosted(results.getTimestamp("Date_Posted"));
	link.setTitle(results.getString("Title"));
	link.setText(results.getString("Text"));
	link.setUrl(results.getString("Url"));
	return link;
    }


    // Bean-style Iterator support
    public void setPageSize(int numLinks)
	throws DataAccessException
    {
	Collection links = find(numLinks);
	this.iterator = links.iterator();
    }

    // Iterator implementation
    public boolean hasNext()
    {
	return iterator.hasNext();
    }

    public Object next()
    {
	return iterator.next();
    }

    public void remove()
    {
	iterator.remove();
    }
}
