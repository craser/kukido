<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page import="net.kukido.blog.datamodel.Link" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="links" class="net.kukido.blog.dataaccess.LinkDao" scope="page">
  <jsp:setProperty name="links" property="pageSize" value="20"/>
</jsp:useBean>
<div class="navitem">
<span class="navtitle">Recent Links</span>
<ul class="navmenu">
<logic:iterate name="links" id="link" type="net.kukido.blog.datamodel.Link">
<li><a href="<%= link.getUrl() %>" title="<%= link.getTitle() %>"><bean:write name="link" property="text"/></a></li>
</logic:iterate>
</ul>
</div>
