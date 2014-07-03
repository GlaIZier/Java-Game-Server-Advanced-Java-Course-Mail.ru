<!DOCTYPE html>
<html>
<head>
   <title>Connection established</title>
</head>
<body>
   <h1 id='head'>
      Hello, ${user.name}, with userID = ${user.id}. Your
      sessionID is <span id='sessionIdHead'>${sessionId}</span>.
   </h1>
   <form name='input' action='game' method='POST'>
      <input type='hidden' name='sessionId' id='sessionId'
         value='${sessionId}'>
   </form>
   <script> 
   		//AJAX 
        function ajaxAsyncRequest(reqURL) { 
        	//Creating a new XMLHttpRequest object
            var xmlhttp = new XMLHttpRequest();
            //Create a asynchronous POST request
            xmlhttp.open('POST', reqURL, true);
            xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');       
            var sessionId = document.getElementById('sessionId').value;
            xmlhttp.send('sessionId=' + sessionId); 
            //When readyState is 4 then get the server output
            xmlhttp.onreadystatechange = function() { 
               if (xmlhttp.readyState == 4) { 
                  if (xmlhttp.status == 200) { 
                     var response = xmlhttp.responseText; 
                     document.getElementById('sessionId').value = response; 
                     document.getElementById('sessionIdHead').innerHTML = response; 
                  } 
                  else { 
                     clearInterval(refresh); 
                     alert('Something is wrong!'); 
                  } 
              } 
           }; 
        }
        var refresh = setInterval(function(){ajaxAsyncRequest("${path}")}, ${ajaxInterval} ) 
  </script>
</body>
</html>