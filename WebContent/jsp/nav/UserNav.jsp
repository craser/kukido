<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%-- This page outputs a list of <li> elements meant to fit within the navigation menu at the top of the page. --%>
<logic:present name="user" scope="session">
  <jsp:useBean id="user" type="net.kukido.blog.datamodel.User" scope="session"/>
  <li><html:link action="LogUpdateForm">New Entry</html:link></li>
  <li><html:link action="UserUpdateForm">Settings</html:link></li>
  <li><html:link action="ViewCommentSpam">Spam</html:link></li>
  <li><html:link action="Logout">Log Out</html:link></li>
</logic:present>
