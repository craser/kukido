<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="sidebarElement">
  <tiles:put type="string" name="title">Marathon Countdown</tiles:put>
  <tiles:put type="string" name="content">
    <form name="mcForm" action="#">
      <fieldset id="countdown">
      <input type="text" id="mc" name="mc" value="loading..." size="12" onfocus="this.blur()" />
      </fieldset>
      <script type="text/JavaScript">
        var SECOND = 1000;
        var MINUTE = 60 * SECOND;
        var HOUR = 60 * MINUTE;
        var DAY = 24 * HOUR;

        function runCountdown(dsp)
        {
          var gap = new Date(2006, 2, 5, 8, 30).getTime() - new Date().getTime();
          var d = 0;
          var h = 0;
          var m = 0;
          var s = 0;
          while (gap >= DAY)    { d++; gap -= DAY;    }
          while (gap >= HOUR)   { h++; gap -= HOUR;   }
          while (gap >= MINUTE) { m++; gap -= MINUTE; }
          while (gap >= SECOND) { s++; gap -= SECOND; }

          dsp.value = pad(d) + ":" + pad(h) + ":" + pad(m) + ":" + pad(s);

          setTimeout("runCountdown(document.mcForm.mc)", 1000);
        }

        function pad(n)
        {
          if (n < 10) return "0" + n;
          else return "" + n;          
        }
        runCountdown(document.mcForm.mc);
      </script>
    </form>
  </tiles:put>
</tiles:insert>
