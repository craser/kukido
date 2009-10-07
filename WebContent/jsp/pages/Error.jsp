<%@ page import="java.io.*" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<bean:define id="exception" name="exception" scope="request" type="Exception" />
<html>
  <head>
    <title>DMG: Server Error</title>
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
    exception.printStackTrace(new PrintWriter(out)); 
}
catch (Exception ignored) {}
%>

    --!>
  </body>
</html>
