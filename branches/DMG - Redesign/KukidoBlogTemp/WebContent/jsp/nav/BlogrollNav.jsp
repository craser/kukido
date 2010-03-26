<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="sidebarElement" flush="true">
  <tiles:put type="string" name="title">Blogroll</tiles:put>
  <tiles:put type="page" name="content" value="/Blogroll" />
</tiles:insert>