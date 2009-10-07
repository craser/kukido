<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Standards are Good</tiles:put>
  <tiles:put type="string" name="content">
    <p><a href="http://browsehappy.com/" 
    title="Browse Happy: Switch to a safer browser today"><img 
    src="img/bh_150x40_anim.gif" alt="Browse Happy logo" width="150" height="40" 
    style="border: 0"></a>
    </p>
    <p><a 
    href="http://www.spreadfirefox.com/?q=affiliates&amp;id=0&amp;t=65"><img
    border="0" alt="Get Firefox!" title="Get Firefox!" style="border: 0"
    src="http://sfx-images.mozilla.org/affiliates/Buttons/110x32/safer.gif"/></a>
    </p>
    <p>
      <a href="http://validator.w3.org/check?uri=referer"><img style="border: 0;"
          src="img/valid-html401.png"
          alt="Valid HTML 4.01 Transitional" height="31" width="88"></a>
    </p>   
  </tiles:put>
</tiles:insert>