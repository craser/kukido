<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="entries" type="java.util.Collection" scope="request"/>
<jsp:useBean id="searchForm" type="net.kukido.blog.forms.SearchForm" scope="request"/>
<tiles:insert definition="pageLayout">
  <tiles:put name="title" type="string">Search Results for "<bean:write name="searchForm" property="searchTerm" />"</tiles:put>
  <tiles:put name="content" type="string">
    <!-- SearchResults.jsp -->
    <logic:greaterThan name="searchForm" property="page" value="1">
      <html:link styleClass="paginglink prevpagelink" page="/search" name="searchForm" property="prevPageParamMap">previous results</html:link>
    </logic:greaterThan>
    <logic:iterate name="entries" id="entry" type="net.kukido.blog.datamodel.LogEntryHeader">
      <div class="searchresult">
        <tiles:insert definition="entry-header" flush="false">
          <tiles:put name="entry" beanName="entry" beanScope="page" />
        </tiles:insert>
      </div>
    </logic:iterate>
    <logic:lessEqual name="searchForm" property="pageSize" value="<%= String.valueOf(entries.size()) %>">
      <html:link styleClass="paginglink nextpagelink" page="/search" name="searchForm" property="nextPageParamMap">more results</html:link>
    </logic:lessEqual>
    <!-- End of SearchResults.jsp -->
  </tiles:put>
  
  <tiles:put type="string" name="sidebar">
    <!-- SearchResults.jsp -->
    <jsp:include page="/jsp/nav/SearchNav.jsp" />
    <logic:iterate name="tags" id="tag" type="net.kukido.blog.datamodel.Tag">
      <bean:define id="tagName" name="tag" property="name" />
      <div class="searchresult">
        <a href="tags/<%= tagName %>"><bean:write name="tag" property="name" /></a>
      </div>
    </logic:iterate>
    <!-- End of SearchResults.jsp -->
  </tiles:put>
</tiles:insert>
