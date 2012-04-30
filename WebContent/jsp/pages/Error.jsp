<%@ page import="java.io.*" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<bean:define id="exception" name="exception" scope="request" type="Throwable" />
<html>
  <head>
    <title>DMG: Server Error</title>
    <dmg:BaseHrefTag />
    <style>
      body {
        text-align: center;
        margin-top: 3em;
      }
    </style>
  </head>
  <body>
    <img src="img/fail-dog.jpg" alt="Server Error Mascot" />
    
    <!--
<% 
try {
	while (exception.getCause() != null) {
		exception = exception.getCause();
	}
    exception.printStackTrace(new PrintWriter(out)); 
}
catch (Exception ignored) {}
%>

    --!>
  </body>
</html>
