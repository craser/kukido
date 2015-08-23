<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<jsp:useBean id="tags" type="java.util.Collection" scope="request"/>
[
	<nested:iterate name="tags" id="tag" type="net.kukido.blog.datamodel.Tag">
		"<nested:write name="tag" property="name" />",
	</nested:iterate>
]