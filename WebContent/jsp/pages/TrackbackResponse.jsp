<%@page pageEncoding="UTF-8"%><%@page contentType="text/xml"%><?xml version="1.0" encoding="utf-8"?>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<%@ page import="org.apache.struts.action.*" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="trackback" type="net.kukido.blog.datamodel.Trackback" scope="request"/>
<jsp:useBean id="errors" type="java.util.Collection" scope="request"/>
<response>
  <logic:empty name="errors">
  <error>0</error>
  </logic:empty>
  
  <logic:notEmpty name="errors">
  <error>1</error>
  <message>
    <logic:iterate id="error" name="errors" type="org.apache.struts.action.ActionMessage">
    <bean:message name="error" property="key" />
    </logic:iterate>
  </message>
  </logic:notEmpty>
</response>
