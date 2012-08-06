<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="entries" type="java.util.Collection" scope="request"/>
<jsp:useBean id="entryListingForm" type="net.kukido.blog.forms.SearchForm" scope="request" />
// json/EntryListing.jsp
/*
LogEntry:
    private int entryId;
    private java.util.Date datePosted;
    private java.util.Date lastUpdated;
    private int userId;
    private String userName;
    private boolean allowComments;
    private String title;
    private String imageFileName;
    private String imageFileType;
    private String intro;
    private String body;
    private String viaTitle;
    private String viaText;
    private String viaUrl;
    private Collection attachments;
    private Collection tags;
    private Collection comments;
    private List trackbacks;
*/
logEntries = [
    <nested:iterate name="entries" id="entry" type="net.kukido.blog.datamodel.LogEntry">
        {   entryId: <nested:write name="entry" property="entryId" />,
            datePosted: "<nested:write name="entry" property="datePosted" />",
            lastUpdated: "<nested:write name="entry" property="lastUpdated" />",
            userId: <nested:write name="entry" property="userId" />,
            userName: "<nested:write name="entry" property="userName" />",
            title: "<nested:write name="entry" property="title" />",
            viaTitle: "<nested:write name="entry" property="viaTitle" />",
            viaUrl: "<nested:write name="entry" property="viaUrl" />"
        },
    </nested:iterate>
    ];
// End of json/LogEntryListing.jsp
