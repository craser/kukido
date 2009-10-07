<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<logic:present name="user" scope="session">
  <jsp:useBean id="user" type="net.kukido.blog.datamodel.User" scope="session"/>
  <tiles:insert definition="sidebarElement">
    <tiles:put type="string" name="title">User: <bean:write name="user" property="userName"/></tiles:put>

    <tiles:put type="string" name="content">
      <ul class="navmenu">
        <li><html:link action="LogUpdateForm">New Log Entry</html:link></li>
        <li><html:link action="MophoUploadForm">Mopho Upload</html:link></li>
        <li><html:link action="UserUpdateForm">Update User Settings</html:link></li>
        <li><html:link action="ViewCommentSpam">View Trapped Spam</html:link></li>
        <li><html:link action="DropBox">Drop Box</html:link></li>
        <li><html:link action="Logout">Log out <bean:write name="user" property="userName"/></html:link></li>
      </ul>
    </tiles:put>
  </tiles:insert>
</logic:present>
