<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:insert definition="frontPageLayout">
  <tiles:put name="head" type="string">
    <link rel="stylesheet" type="text/css" href="css/blogsidebar.css" />
  </tiles:put>
  <tiles:put name="content" type="string">
    <!-- EntryListing.jsp -->
    <jsp:useBean id="entries" type="java.util.Collection" scope="request"/>
    <jsp:useBean id="entryListingForm" type="net.kukido.blog.forms.SearchForm" scope="request" />
    <logic:greaterThan name="entryListingForm" property="page" value="1">
      <bean:define id="prevPageUrl"><%= "/" + entryListingForm.getPreviousPage() %></bean:define>
      <html:link styleClass="paginglink prevpagelink" page="${prevPageUrl}">newer stuff</html:link>
    </logic:greaterThan>
    <logic:iterate name="entries" id="entry" type="net.kukido.blog.datamodel.LogEntryHeader">
      <tiles:insert definition="entry" flush="false">
        <tiles:put name="entry" beanName="entry" beanScope="page" />
        <tiles:put name="showContent" value="true" beanScope="page" />
        <tiles:put name="showBody" value="false" beanScope="page" />
      </tiles:insert>
    </logic:iterate>
    <logic:lessEqual name="entryListingForm" property="pageSize" value="<%= String.valueOf(entries.size()) %>">
      <bean:define id="nextPageUrl"><%= "/" + entryListingForm.getNextPage() %></bean:define>
      <html:link styleClass="paginglink nextpagelink" page="${nextPageUrl}">older stuff</html:link>
    </logic:lessEqual>
    <!-- End of LogEntryListing.jsp -->
  </tiles:put>
</tiles:insert>
