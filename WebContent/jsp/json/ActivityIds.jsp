<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<jsp:useBean id="attachments" type="java.util.Collection" scope="request" />
[
<nested:size name="attachments" id="numAttachments" />
<nested:iterate id="attachment" name="attachments" type="net.kukido.blog.datamodel.Attachment" indexId="aIndex">
{ "activityId": "<nested:write name="attachment" property="activityId" />",
  "fileName": "<nested:write name="attachment" property="fileName" />",
  "title": "<nested:write name="attachment" property="title" />"
}<nested:notEqual name="aIndex" value="<%= String.valueOf(numAttachments.intValue() - 1) %>">,</nested:notEqual>
</nested:iterate>
]

