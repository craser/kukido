<%@ page import="net.kukido.blog.datamodel.*" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<jsp:useBean class="java.util.Date" id="now" />

<tiles:insert definition="pageLayout">

  <tiles:put type="string" name="head">
    <link rel="stylesheet" type="text/css" href="css/logedit.css">
    <script type="text/JavaScript" src="javascript/ajax.js"> </script>
    <script type="text/JavaScript" src="javascript/tags-typeahead.js"> </script>
    <script type="text/JavaScript">
    window.addEventHandler("load", TagsTypeAhead.init("tagsInput"));
    </script>
  </tiles:put>

  <tiles:put type="string" name="content">
    <nested:form action="LogUpdate.do" focus="entry.title">
      
      <html:errors/>
      
      <div class="entry">

        <div class="title">
          <nested:text property="entry.title" size="60"/>
        </div>

        <div class="entrymetainfo">
          <bean:write name="now" format="EEE, M/d h:mma" />
        </div>

        <div class="entrycontent">          
          
          <nested:present property="entry.imageFileName">
            <nested:define id="thumbnailFilename" name="logUpdateForm" property="entry.imageFileName" />
            <img class="thumbnail" src="attachments/thumbs/<%= thumbnailFilename %>">
          </nested:present>
          
          <table>
            <tr>
              <td class="inputlabel">Via (title):</td>
              <td><nested:text property="entry.viaTitle"/></td>
            </tr>
            <tr>
              <td class="inputlabel">Via (text):</td>
              <td><nested:text property="entry.viaText"/></td>
            </tr>
            <tr>
              <td class="inputlabel">Via (URL):</td>
              <td><nested:text property="entry.viaUrl"/></td>
            </tr>
            <tr>
              <td class="inputlabel">Tags (space-separated):</td>
              <td><nested:text property="tags" styleId="tagsInput" /></td>
            </tr>
            <tr>
              <td class="inputlabel">Allow Comments:</td>
              <td><nested:checkbox property="entry.allowComments" /></td>
            </tr>
            <tr>
              <td class="inputlabel">Syndicate:</td>
              <td><nested:checkbox property="entry.syndicate" /></td>
            </tr>
          </table>
          
          <nested:textarea property="entry.intro" rows="20" cols="60"/>
          <nested:textarea property="entry.body" rows="20" cols="60"/>
        </div>
        <nested:image src="img/icon_save.png" title="Save Changes" />
      </div>      
    </nested:form>
  </tiles:put>
  
</tiles:insert>





