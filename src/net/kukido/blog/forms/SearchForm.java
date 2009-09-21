package net.kukido.blog.forms;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.util.*;

/**
 * This is used for multiple Actions
 **/
public class SearchForm extends ActionForm
{
    static private final int DEFAULT_PAGE = 1;
    static private final int DEFAULT_PAGE_SIZE = 30; // 0-indexed
    static private final int DEFAULT_USER_ID = -1;
    static private final int DEFAULT_YEAR = -1;
    static private final int DEFAULT_MONTH = -1;
    static private final int DEFAULT_DATE = -1;
    
    // Note that if any of these default Strings are changed to non-null
    // values, you must alter getParamMap() to use equals() instead of 
    // !=
    static private final String DEFAULT_SEARCH_TERM = null;
    static private final String DEFAULT_TAG_NAMES = null;
    
    private int page;		// 0-indexed.
    private int pageSize;	// Number of entries per page
    private int userId;		// User for which to find entries
    private String searchTerm;   // String to match against text of entry

    // Year, month, and date in forms understood by java.util.Calendar
    private int year;           // Year
    private int month;          // Month
    private int date;           // Day of Month
    private String tagNames;    // Tags for which to retrieve entries
    
    public SearchForm()
    {
    }        

    public void reset(ActionMapping mapping, HttpServletRequest req)
    {
	this.page = DEFAULT_PAGE;
	this.pageSize = DEFAULT_PAGE_SIZE;
        this.userId = DEFAULT_USER_ID;
	this.searchTerm = DEFAULT_SEARCH_TERM;
        this.tagNames = DEFAULT_TAG_NAMES;
        this.year = DEFAULT_YEAR;
        this.month = DEFAULT_MONTH;
        this.date = DEFAULT_DATE;
    }
    
    public void setPage(int page)
    {
	this.page = Math.max(page, 1);
    }

    public int getPage()
    {
	return page;
    }
    
    public int getNextPage()
    {
        return page + 1;
    }
    
    public int getPreviousPage()
    {
        return page - 1;
    }
    
    public void setPageSize(int pageSize)
    {
	this.pageSize = Math.max(pageSize, 1);
    }

    public int getPageSize()
    {
	return pageSize;
    }
	
    public void setUserId(int userId)
    {
	this.userId = userId;
    }

    public int getUserId()
    {
	return userId;
    }

    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm()
    {
	return searchTerm;
    }

    public void setYear(int year)
    {
        this.year = (year > 0) ? year : -1;
    }

    public int getYear()
    {
        return year;
    }

    public void setMonth(int month)
    {
        this.month = (month > 0) ? month : -1;
    }

    public int getMonth()
    {
        return month;
    }

    public void setDate(int date)
    {
        this.date = (date > 0) ? date : -1;
    }

    public int getDate()
    {
        return date;
    }
    
    public void setTags(String tagNames)
    {
        this.tagNames = tagNames;
    }
    
    public String getTags()
    {
        return tagNames;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req)
    {
        return new ActionErrors();
    }
    
    public Map getParamMap()
    {
        // Note that if the default values for the various String properties
        // are changed to non-null values, this method must be altered to use
        // equals() instead of !=
        Map m = new HashMap();
        if (page != DEFAULT_PAGE) m.put("page", new Integer(page));
        if (pageSize != DEFAULT_PAGE_SIZE) m.put("pageSize", new Integer(pageSize));
        if (userId != DEFAULT_USER_ID) m.put("userId", new Integer(userId));
        if (searchTerm != DEFAULT_SEARCH_TERM) m.put("searchTerm", searchTerm);
        if (year != DEFAULT_YEAR) m.put("year", new Integer(year));
        if (month != DEFAULT_MONTH) m.put("month", new Integer(month));
        if (date != DEFAULT_DATE) m.put("date", new Integer(date));
        if (tagNames != DEFAULT_TAG_NAMES) m.put("tags", tagNames);
                
        return m;
    }
    
    public Map getNextPageParamMap()
    {
        Map m = getParamMap();
        m.put("page", new Integer(page + 1));
        return m;
    }
    
    public Map getPrevPageParamMap()
    {
        Map m = getParamMap();
        m.put("page", new Integer(page - 1));
        return m;
    }
}
