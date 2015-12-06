<%@ page contentType="text/plain" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<jsp:useBean id="status" type="java.lang.String" scope="request" />
<jsp:useBean id="message" type="java.lang.String" scope="request" />
<%
org.apache.log4j.Logger log = net.kukido.blog.log.Logging.getLogger("net.kukido.blog.action.GpsImport");
log.info("Running GpsImportResults.jsp");
%>
{ "status": "<bean:write name="status"/>",
  "message": "<bean:write name="message"/>"
}