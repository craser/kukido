<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<jsp:useBean id="activityIds" type="java.util.Collection" scope="request" />
[
<nested:size name="activityIds" id="numIds" />
<nested:iterate id="id" name="activityIds" type="String" indexId="idIndex">
"<nested:write name="id" />"<nested:notEqual name="idIndex" value="<%= String.valueOf(numIds.intValue() - 1) %>">,</nested:notEqual>
</nested:iterate>
]

