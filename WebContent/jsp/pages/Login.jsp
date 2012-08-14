<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="java.io.*" %>
<tiles:insert definition="pageLayout">
<tiles:put type="string" name="content">
<!-- Login.jsp -->
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:form action="Login" focus="username">
<table>
<tr>
  <td class="inputlabel">Username:</td>
  <td><html:text property="username" size="10"/></td>
</tr>
<tr>
  <td class="inputlabel">Password:</td>
  <td><html:password property="password" size="10"/></td>
</tr>
<tr>
  <td class="inputlabel">Remember?</td>
  <td><html:checkbox property="remember" value="true" /></td>
</tr>
<tr>
  <td colspan="2"><html:submit title="login" value="login"/></td>
</tr>
</table>
</html:form>

<!-- bling
<% 
try {
  Throwable exception = (Throwable)request.getAttribute("exception");
  while (exception.getCause() != null) {
    exception = exception.getCause();
  }
    exception.printStackTrace(new PrintWriter(out)); 
}
catch (Exception ignored) {}
%>

--!>
<!-- end of Login.jsp -->
</tiles:put>
</tiles:insert>
