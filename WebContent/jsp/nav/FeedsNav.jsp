<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Feeds (RSS 2.0)</tiles:put>
  <tiles:put type="string" name="content">
    <ul class="navmenu feedsmenu">
      <li><a href="monkeyfeed.xml">DreadedMonkeyGod.net</a></li>
      <li><a href="http://del.icio.us/rss/DeathBeforeDecaf">del.icio.us links</a></li>
    </ul>
  </tiles:put>
</tiles:insert>
