<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>

<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Comment Administration</tiles:put>
  <tiles:put type="string" name="content">
    <ul class="navmenu">
      <li><html:link href="SetCommentsEnabled.do?enabled=true">Enable Comments</html:link></li>
      <li><html:link href="SetCommentsEnabled.do?enabled=false"><em>Dis</em>able Comments</html:link></li>
    </ul>
  </tiles:put>
</tiles:insert>