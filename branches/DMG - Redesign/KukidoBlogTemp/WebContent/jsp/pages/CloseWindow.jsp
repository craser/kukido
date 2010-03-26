<% System.out.println("CloseWindow called!"); %>
<html>
  <head>
    <title>Action Complete</title>
    <style>
      body { text-align: center; }
      a { font-size: 2em; }
    </style>
  </head>
  <body onload="window.close()">
    <p>Action completed successfully.</p>
    <p><a href="javascript:window.close()">Close Window</a></p>
  </body>
</html>
<% System.out.println("FLUSHING!"); %>
<% out.flush(); // Dust off and nuke 'em from orbit.  It's the only way to be sure. %>