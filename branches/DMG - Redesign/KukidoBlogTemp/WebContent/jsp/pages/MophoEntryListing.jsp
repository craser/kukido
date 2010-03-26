<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:insert definition="pageLayout">
<tiles:put type="string" name="content">
<!-- MophoEntryListing.jsp -->
<jsp:useBean id="entries" type="java.util.Collection" scope="request"/>
<jsp:useBean id="pageNumber" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="pageSize" type="java.lang.Integer" scope="request"/>
<logic:greaterThan name="pageNumber" value="0">
<a class="paginglink" href="mopho/<%= pageNumber.intValue() - 1 %>">&laquo; newer stuff</a>      
</logic:greaterThan>
<logic:iterate name="entries" id="entry" type="net.kukido.blog.datamodel.MophoEntry">
<div class="entry">
<span class="title"><bean:write name="entry" property="title"/></span>
<span class="entrymetainfo">Posted: <bean:write name="entry" property="datePosted"/></span>
<span class="entrycontent">
<img src="mopho/<%= entry.getFileName() %>" alt="<%= entry.getTitle() %>" />
</span>
</div>
</logic:iterate>
<logic:lessEqual name="pageSize" value="<%= String.valueOf(entries.size()) %>">
<a class="paginglink" href="mopho/<%= pageNumber.intValue() + 1 %>">older stuff &raquo;</a>
</logic:lessEqual>
<!-- End of MophoEntryListing.jsp -->
</tiles:put>
</tiles:insert>
