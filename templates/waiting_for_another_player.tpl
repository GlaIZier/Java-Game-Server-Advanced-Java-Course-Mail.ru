<!DOCTYPE html>
<html>
<head>
   <title>Waiting for another player</title>
   <script type='text/javascript' src='js/check_for_another_player.js'></script>
   <script> ConnectionHolder.start('${path}', ${ajaxInterval})  </script>
</head>
<body>
   <h1 id='head'>
      Hello, ${user.name}, with userId = <span id='userId'>${user.id}</span>. Your
      sessionId is <span id='sessionIdHead'>${sessionId}</span>. Wait for another player.
   </h1>
</body>
</html>

<!--   <form name='input' action='game' method='POST'>
      <input type='hidden' name='sessionId' id='sessionId'
         value='${sessionId}'>
   </form>
-->   
   