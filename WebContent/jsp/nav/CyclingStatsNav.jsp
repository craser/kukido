<%@ page contentType="application/xhtml+xml" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Cycling Miles:</tiles:put>
  <tiles:put type="string" name="content">
    <ul class="navmenu">
      <li>Year-to-Date: <bean:write name="ytdMiles" /></li>
      <li>This Month: <bean:write name="mtdMiles" /></li>
    </ul>
  </tiles:put>
</tiles:insert>