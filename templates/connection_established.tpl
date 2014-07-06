<!DOCTYPE html>
<html>
<head>
   <title>Connection established</title>
   <script type='text/javascript' src='js/connection_established.js'></script>
   <script> ConnectionHolder.start('${path}', ${ajaxInterval})  </script>
</head>
<body>
   <h1 id='head'>
      Hello, ${user.name}, with userId = ${user.id}. Your
      sessionId is <span id='sessionIdHead'>${sessionId}</span>.
   </h1>
</body>
</html>

<!--   <form name='input' action='game' method='POST'>
      <input type='hidden' name='sessionId' id='sessionId'
         value='${sessionId}'>
   </form>
-->   
   