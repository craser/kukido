<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="net.kukido.blog.datamodel.*" %>
<jsp:useBean id="tags" type="java.util.Collection" scope="request"/>
[
    <nested:size name="tags" id="numTags" />
	<nested:iterate name="tags" id="tag" type="net.kukido.blog.datamodel.Tag" indexId="tagIndex">
		"<nested:write name="tag" property="name" />"<nested:notEqual name="tagIndex" value="<%= String.valueOf(numTags.intValue() - 1) %>">,</nested:notEqual>
	</nested:iterate>
]