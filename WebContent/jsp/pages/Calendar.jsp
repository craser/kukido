<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean id="today" class="java.util.GregorianCalendar" />
<jsp:useBean id="cal" class="java.util.GregorianCalendar" />

<style>

th {
  font-weight: normal;
}

.current {
  background-color: #eee;
}

.adjacent {
  background-color: gray;
}

</style>

<% int currentMonth = cal.get(cal.MONTH); %>
<% cal.set(cal.DAY_OF_MONTH, 1); %>
<% while (cal.get(cal.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) { cal.add(cal.DATE, -1); } %>
<div class="navitem">
<table border="0" cellspacing="1" cellpadding="2">
  <tr>
    <th colspan="7">
      <bean:write name="today" property="time" format="MMMM" />
    </th>
  </tr>
  <% while (cal.before(today) || cal.get(cal.MONTH) <= currentMonth) { %>
    <tr>
      <% do { %>
        <% String styleClass = (cal.get(cal.MONTH) == currentMonth) ? "current" : "adjacent"; %>
        <td class="<%= styleClass %>"><bean:write name="cal" property="time" format="dd" /></td>
        <% cal.add(cal.DATE, 1); %>
      <% } while (cal.get(cal.DAY_OF_WEEK) != cal.getFirstDayOfWeek()); %>
    </tr>
  <% } %>
</table>
</div>
