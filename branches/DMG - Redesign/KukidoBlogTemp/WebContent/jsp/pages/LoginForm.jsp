<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:errors/>
<html:form action="Login.do" focus="username">
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
