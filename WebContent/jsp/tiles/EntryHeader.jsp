<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>
<%@ taglib uri="/WEB-INF/dmg-util.tld" prefix="dmg" %>
<tiles:useAttribute id="entry" name="entry" classname="net.kukido.blog.datamodel.LogEntryHeader" />

<!-- EntryHeader Tile -->
<h2 class="title" style="border: 0; margin-top: 1em;"><dmg:entrylink entry="entry"><span style="color: #fff; opacity: 0.6;"><bean:write name="entry" property="datePosted" format="MM/dd/yy" /></span> <bean:write name="entry" property="title" /></dmg:entrylink></h2>
<span class="entrymetainfo" style="padding: 2px;">
  | comments: <bean:write name="entry" property="numComments" /> |
  <logic:notEmpty name="entry" property="tags">
    <span style="font-variant: small-caps; text-transform: lowercase;">
      Tags:
      <logic:iterate name="entry" property="tags" id="tag" type="net.kukido.blog.datamodel.Tag">
        <a href="tags/<%= tag %>"><bean:write name="tag" property="name" /></a>
      </logic:iterate>
    </span>
  </logic:notEmpty>
</span>
<!-- /EntryHeader Tile -->