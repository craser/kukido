package net.kukido.blog.dataaccess;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

import net.kukido.blog.datamodel.*;
import net.kukido.blog.forms.*;
import net.kukido.sql.*;

/**
 * Implements CRUD design pattern: Create, Retrieve, Update, Delete.
 * */
public class LogDao extends Dao implements Iterator
{
    private Iterator iterator; // Populated by setX() methods.

    private static final String CREATE_SQL = "insert into LOG_ENTRIES "
            + "(Date_Posted, Last_Updated, User_ID, User_Name, Title"
            + ",Image_File_Name ,Image_File_Type ,Intro, Body, Via_Text, Via_Url, Via_Title, Allow_Comments)"
            + " values " + "(:Date_Posted" // Date_Posted
            + ",:Last_Updated" // Last_Updated
            + ",:User_ID" // User_ID
            + ",:User_Name" // User_Name
            + ",:Title" // Title
            + ",:Image_File_Name" // Image_File_Name
            + ",:Image_File_Type" // Image_File_Type
            + ",:Intro" // Intro
            + ",:Body" // Body
            + ",:Via_Text" // Via_Text
            + ",:Via_Url" // Via_Url
            + ",:Via_Title" // Via_Title
            + ",:Allow_Comments" // Allow_Comments
            + ")";

    private static final String UPDATE_SQL = "update LOG_ENTRIES set"
            + " Date_Posted = :Date_Posted" // 1 Date_Posted
            + ",Last_Updated = :Last_Updated" + ",User_ID = :User_ID" // 2
                                                                      // User_ID
            + ",User_Name = :User_Name" // 3 User_Name
            + ",Title = :Title" // 6 Title
            + ",Image_File_Name = :Image_File_Name" // 7 Image_File_Name
            + ",Image_File_Type = :Image_File_Type" // 8 Image_File_Type
            + ",Intro = :Intro" // 9 Intro
            + ",Body = :Body" // 10 Body
            + ",Via_Title = :Via_Title" // 11 Via_Title
            + ",Via_Text = :Via_Text" // 12 Via_Text
            + ",Via_Url = :Via_Url" // 13 Via_Url
            + ",Allow_Comments = :Allow_Comments" // 14 Allow_Updates
            + " where Entry_ID = :Entry_ID"; // 15 Entry_ID

    private static final String DELETE_SQL = "delete from LOG_ENTRIES where Entry_ID = :Entry_ID"; // 1
                                                                                                   // Entry_ID

    private static final String FIND_DATES_POSTED_SQL = "select distinct"
            + " year(Date_Posted) Year" + ",month(Date_Posted) Month"
            + ",dayofmonth(Date_Posted) Date" + " from LOG_ENTRIES" + " order by Date_Posted desc";

    private static final String FIND_ENTRY_AFTER_ID_SQL = "select min(Entry_ID) from LOG_ENTRIES where Entry_ID > :Entry_ID";

    private static final String FIND_ENTRY_BEFORE_ID_SQL = "select max(Entry_ID) from LOG_ENTRIES where Entry_ID < :Entry_ID";

    private static final String FIND_BY_ENTRY_ID = "select * from LOG_ENTRIES where Entry_ID = :Entry_ID";

    private static final String FIND_BY_ENTRY_IDS_SQL_FORMAT = "select * from LOG_ENTRIES where Entry_ID in {0}";

    /**
     * Creates a new LogEntry from the information in the given entry.
     */
    public LogEntry create(LogEntry logEntry) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement create = null;
        try {
            conn = getConnection();
            create = new NamedParamStatement(conn, CREATE_SQL);
            Timestamp now = new Timestamp(new java.util.Date().getTime());
            create.setTimestamp("Date_Posted", now);
            create.setTimestamp("Last_Updated", now);
            create.setInt("User_ID", logEntry.getUserId());
            create.setString("User_Name", logEntry.getUserName());
            create.setString("Title", logEntry.getTitle());
            create.setString("Image_File_Name", logEntry.getImageFileName());
            create.setString("Image_File_Type", typeString(logEntry.getImageFileType()));
            create.setString("Intro", logEntry.getIntro());
            create.setString("Body", logEntry.getBody());
            create.setString("Via_Title", logEntry.getViaTitle());
            create.setString("Via_Text", logEntry.getViaText());
            create.setString("Via_Url", logEntry.getViaUrl());
            create.setString("Allow_Comments", Boolean.toString(logEntry.getAllowComments()));
            create.executeUpdate();

            logEntry.setEntryId(getLastCreatedId(conn));

            // I completely forgot that I wrote this. Must use it sometime.
            try {
                new LinkDao().create(logEntry);
            }
            catch (Exception ignored) {
            }

            new TagLinkDao().assignTags(logEntry.getEntryId(), logEntry.getTags());

            new TrackbackDao().sendTrackbacks(logEntry);

            return findByEntryId(logEntry.getEntryId());
        }
        catch (SQLException e) {
            throw new DataAccessException("Caught while creating Log Entry", e);
        }
        finally {
            try {
                create.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    public void delete(int entryId) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement delete = null;
        try {
            new AttachmentDao().deleteByEntryId(entryId);
            new TagLinkDao().deleteByObjectId(entryId);
            new CommentDao().deleteByEntryId(entryId);

            conn = getConnection();
            delete = new NamedParamStatement(conn, DELETE_SQL);
            delete.setInt("Entry_ID", entryId);
            delete.executeUpdate();

        }
        catch (SQLException e) {
            throw new DataAccessException("Caught while deleting log entry with ID " + entryId, e);
        }
        finally {
            try {
                delete.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    public void update(LogEntry logEntry) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement update = null;
        try {
            conn = getConnection();
            update = new NamedParamStatement(conn, UPDATE_SQL);
            update.setTimestamp("Date_Posted", new java.sql.Timestamp(logEntry.getDatePosted()
                    .getTime()));
            update.setTimestamp("Last_Updated",
                    new java.sql.Timestamp(new java.util.Date().getTime()));
            update.setInt("User_ID", logEntry.getUserId());
            update.setString("User_Name", logEntry.getUserName());
            update.setString("Title", logEntry.getTitle());
            update.setString("Image_File_Name", logEntry.getImageFileName());
            update.setString("Image_File_Type", typeString(logEntry.getImageFileType()));
            update.setString("Intro", logEntry.getIntro());
            update.setString("Body", logEntry.getBody());
            update.setInt("Entry_ID", logEntry.getEntryId());
            update.setString("Via_Title", logEntry.getViaTitle());
            update.setString("Via_Text", logEntry.getViaText());
            update.setString("Via_Url", logEntry.getViaUrl());
            update.setString("Allow_Comments", Boolean.toString(logEntry.getAllowComments()));

            update.executeUpdate();

            new AttachmentDao().updateAll(logEntry.getAttachments());
            new TagLinkDao().assignTags(logEntry.getEntryId(), logEntry.getTags());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to update Log Entry #" + logEntry.getEntryId(), e);
        }
        finally {
            try {
                update.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }
    
    private String typeString(Attachment.FileType fileType) {
    	return (fileType == null) ? null : fileType.toString();
    }

    public Collection findByEntryIds(Collection entryIds) throws DataAccessException
    {
        Connection conn = null;
        PreparedStatement find = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = MessageFormat.format(FIND_BY_ENTRY_IDS_SQL_FORMAT,
                    new String[] { buildParamList(entryIds.size()) });
            find = conn.prepareStatement(sql);
            int p = 1;
            for (Iterator i = entryIds.iterator(); i.hasNext(); p++) {
                int id = ((Integer) i.next()).intValue();
                find.setInt(p, id);
            }

            rs = find.executeQuery();
            return populateEntries(rs);
        }
        catch (Exception e) {
            throw new DataAccessException("No entry found with Entry_IDs " + entryIds, e);
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignored) {
            }
            try {
                find.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    public LogEntry findByEntryId(int entryId) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_ENTRY_ID);
            find.setInt("Entry_ID", entryId);

            rs = find.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException("No entry found with Entry_ID " + entryId);
            }

            LogEntry entry = populateEntry(rs);
            return entry;
        }
        catch (Exception e) {
            throw new DataAccessException("No entry found with Entry_ID " + entryId, e);
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignored) {
            }
            try {
                find.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    /**
     * Returns a Collection of LogEntryHeader
     * 
     * @param form
     * @return Collection of LogEntryHeader
     * @throws net.kukido.blog.dataaccess.DataAccessException
     */
    public Collection find(SearchForm form) throws DataAccessException
    {
        List tables = new ArrayList();
        List criteria = new ArrayList();
        List fields = new ArrayList();
        Map params = new HashMap();

        if (form.getTags() != null) {
            StringTokenizer tagTokens = new StringTokenizer(form.getTags());
            Collection tagNames = new ArrayList();
            while (tagTokens.hasMoreTokens())
                tagNames.add(tagTokens.nextToken());

            Collection tags = new TagDao().findByNames(tagNames);
            int start = (form.getPage() - 1) * form.getPageSize();
            int pageSize = form.getPageSize();
            Collection entryIds = new TagLinkDao().findLinksByTagSet(tags, tags.size());

            StringBuffer c = new StringBuffer(); // c for Criterion
            c.append("LOG_ENTRIES.Entry_ID in (");
            for (Iterator i = entryIds.iterator(); i.hasNext();) {
                Integer entryId = (Integer) i.next();
                c.append(entryId);
                if (i.hasNext())
                    c.append(",");
            }
            c.append(")");
            criteria.add(c.toString());
        }
        else if (form.getSearchTerm() != null) {
            // There are three distinct names in the query to work
            // around a bug in net.kukido.sql.NamedParamStatement which
            // causes borkage if you try to use the same name more
            // than once.
            criteria.add("(LOG_ENTRIES.Title like :Search_TermA OR LOG_ENTRIES.Intro like :Search_TermB OR LOG_ENTRIES.Body like :Search_TermC)");
            params.put("Search_TermA", "%" + form.getSearchTerm() + "%");
            params.put("Search_TermB", "%" + form.getSearchTerm() + "%");
            params.put("Search_TermC", "%" + form.getSearchTerm() + "%");
        }
        else if (form.getYear() != -1) {
            criteria.add("year(Date_Posted) = :Year");
            params.put("Year", new Integer(form.getYear()));
        }

        if (form.getMonth() != -1) {
            criteria.add("month(Date_Posted) = :Month");
            params.put("Month", new Integer(form.getMonth()));
        }

        if (form.getDate() != -1) {
            criteria.add("dayofmonth(Date_Posted) = :Date");
            params.put("Date", new Integer(form.getDate()));
        }

        if (form.getUserId() >= 0) {
            criteria.add("User_ID = :User_ID");
            params.put("User_ID", new Integer(form.getUserId()));
        }

        StringBuffer select = new StringBuffer("select LOG_ENTRIES.*");
        for (Iterator i = fields.iterator(); i.hasNext();) {
            select.append(", ");
            select.append((String) i.next());
        }

        select.append(" from LOG_ENTRIES");

        for (Iterator i = tables.iterator(); i.hasNext();) {
            select.append(", ");
            select.append((String) i.next());
        }

        if (!criteria.isEmpty())
            select.append(" where ");

        for (Iterator i = criteria.iterator(); i.hasNext();) {
            select.append((String) i.next());
            if (i.hasNext())
                select.append(" and ");
        }

        select.append(" order by Entry_ID desc");

        if (form.getPageSize() > 0) {
            select.append(" limit :Start_Index,:Num_Entries");
            params.put("Num_Entries", new Integer(form.getPageSize()));
            params.put("Start_Index", new Integer((form.getPage() - 1) * form.getPageSize()));
        }

        return find(select.toString(), params);
    }

    /**
     * Returns a Collection of LogEntryHeader
     * 
     * @param select
     * @param params
     * @return Collection of LogEntryHeader
     * @throws net.kukido.blog.dataaccess.DataAccessException
     */
    private Collection find(String select, Map params) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement statement = null;
        ResultSet results = null;
        try {
            conn = getConnection();
            statement = new NamedParamStatement(conn, select);
            statement.bindAll(params);
            results = statement.executeQuery();

            return populateEntries(results);
        }
        catch (SQLException e) {
            throw new DataAccessException("Caught while executing query:", e);
        }
        finally {
            try {
                results.close();
            }
            catch (Exception ignored) {
            }
            try {
                statement.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    public int findEntryAfter(int entryId) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement statement = null;
        ResultSet results = null;
        try {
            conn = getConnection();
            statement = new NamedParamStatement(conn, FIND_ENTRY_AFTER_ID_SQL);
            statement.setInt("Entry_ID", entryId);
            results = statement.executeQuery();
            if (!results.next()) {
                throw new DataAccessException("Can't find entry after ID " + entryId);
            }

            int entryAfter = results.getInt(1);
            return entryAfter;
        }
        catch (DataAccessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataAccessException("Error finding entry after ID " + entryId, e);
        }
    }

    public int findEntryBefore(int entryId) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement statement = null;
        ResultSet results = null;
        try {
            conn = getConnection();
            statement = new NamedParamStatement(conn, FIND_ENTRY_BEFORE_ID_SQL);
            statement.setInt("Entry_ID", entryId);
            results = statement.executeQuery();
            if (!results.next()) {
                throw new DataAccessException("Can't find entry before ID " + entryId);
            }

            int entryAfter = results.getInt(1);
            return entryAfter;
        }
        catch (DataAccessException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataAccessException("Error finding entry before ID " + entryId, e);
        }
    }

    private Collection populateEntries(ResultSet results) throws SQLException, DataAccessException
    {
        Collection entries = new LinkedList();
        while (results.next()) {
            LogEntry entry = new LogEntry();
            populateEntryHeader(results, entry);
            entries.add(entry);
        }

        populateEntryDetails(entries);

        return entries;
    }

    private void populateEntryHeader(ResultSet results, LogEntryHeader header) throws SQLException,
            DataAccessException
    {
        header.setEntryId(results.getInt("Entry_ID"));
        header.setDatePosted(results.getTimestamp("Date_Posted"));
        header.setLastUpdated(results.getTimestamp("Last_Updated"));
        header.setUserId(results.getInt("User_ID"));
        header.setUserName(results.getString("User_Name"));
        header.setTitle(results.getString("Title"));
        header.setImageFileName(results.getString("Image_File_Name"));
        if (results.getString("Image_File_Type") != null) {
        	header.setImageFileType(Attachment.FileType.valueOf(results.getString("Image_File_Type")));
        }
        header.setIntro(results.getString("Intro"));
        header.setBody(results.getString("Body"));
        header.setViaTitle(results.getString("Via_Title"));
        header.setViaText(results.getString("Via_Text"));
        header.setViaUrl(results.getString("Via_Url"));
        header.setAllowComments(results.getBoolean("Allow_Comments"));
    }

    private void populateEntryDetails(Collection entries) throws SQLException, DataAccessException
    {
        Set entryIds = getEntryIds(entries);

        // Attachments
        Map attachmentsById = new AttachmentDao().findByEntryIds(entryIds);
        Map commentsById = new CommentDao().findByEntryIds(entryIds);
        Map trackbacksById = new TrackbackDao().findByEntryIds(entryIds);
        Map tagsById = new TagDao().findByObjectIds(entryIds);

        for (Iterator i = entries.iterator(); i.hasNext();) {
            LogEntry entry = (LogEntry) i.next();
            Integer id = new Integer(entry.getEntryId());
            if (attachmentsById.containsKey(id)) {
                Collection attachments = (Collection) attachmentsById.get(id);
                entry.setAttachments(attachments);
            }
            if (commentsById.containsKey(id)) {
                Collection comments = (Collection) commentsById.get(id);
                entry.setComments(comments);
            }
            if (trackbacksById.containsKey(id)) {
                List trackbacks = (List) trackbacksById.get(id);
                entry.setTrackbacks(trackbacks);
            }
            if (tagsById.containsKey(id)) {
                Collection tags = (Collection) tagsById.get(id);
                entry.setTags(tags);
            }
        }
    }

    private Set getEntryIds(Collection entries)
    {
        Set entryIds = new HashSet();
        for (Iterator i = entries.iterator(); i.hasNext();) {
            LogEntryHeader header = (LogEntryHeader) i.next();
            Integer entryId = new Integer(header.getEntryId());
            entryIds.add(entryId);
        }
        return entryIds;
    }

    /**
     * Creates a new LogEntry and populates it with the data from the current
     * row of given ResultSet.
     */
    private LogEntry populateEntry(ResultSet results) throws SQLException, DataAccessException
    {
        LogEntry entry = new LogEntry();
        populateEntryHeader(results, entry);

        Collection tags = new TagLinkDao().findTagsByObjectId(entry.getEntryId());
        entry.setTags(tags);

        Collection attachments = new AttachmentDao().findByEntryId(results.getInt("Entry_ID"));
        entry.setAttachments(attachments);

        Collection comments = new CommentDao().findByEntryId(entry.getEntryId());
        entry.setComments(comments);

        Collection trackbacks = new TrackbackDao().findByEntryId(entry.getEntryId());
        entry.setTrackbacks(new ArrayList(trackbacks));

        return entry;
    }

    public void setUserId(int userId) throws net.kukido.blog.dataaccess.DataAccessException
    {
        SearchForm searchForm = new SearchForm();
        searchForm.setUserId(userId);
        this.iterator = new LogDao().find(searchForm).iterator();
    }

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
