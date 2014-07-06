<!DOCTYPE html>
<html>
<head>
    <title>Waiting for authentication</title>
    <script type='text/javascript' src='js/wait_auth.js'></script>
    <script> Refresh.start(${refreshInterval}) </script>
</head>
<body> 
	<h1>Waiting for authentication for sessionID: ${sessionId}...</h1> 
  	<form name='input' method='post'> 
    	<input type='hidden' name='login' value='${userName}'> 
        <input type='submit' value='Submit' style='visibility:hidden'> 
    </form> 
</body>