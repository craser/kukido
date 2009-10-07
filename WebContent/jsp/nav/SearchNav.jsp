<%@ page contentType="application/xhtml+xml" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="content">
    <html:form action="SearchLogEntries" method="GET">
      <table class="searchform">
        <tr>
          <td>
            <logic:empty name="searchForm" property="searchTerm">
              <html:text property="searchTerm" size="10" value="Search" onfocus="this.value=''" />
            </logic:empty>
            <logic:notEmpty name="searchForm" property="searchTerm">
              <html:text property="searchTerm" size="10" />
            </logic:notEmpty>
          </td>
          <td>
            <input type="hidden" name="pageSize" value="30" />
            <html:submit value="GO" />
          </td>      
        </tr>
      </table>
    </html:form>
  </tiles:put>
</tiles:insert>
