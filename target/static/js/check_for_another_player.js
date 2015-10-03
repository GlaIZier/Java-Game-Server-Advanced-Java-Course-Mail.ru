
var ConnectionHolder = ConnectionHolder || (function () {

   var _refresher;
   
   var _ajaxRequest = function(reqUrl) {
      //Creating a new XMLHttpRequest object
      var xmlhttp = new XMLHttpRequest();
      //Create a asynchronous POST request
      xmlhttp.open('POST', reqUrl, true);
      xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');       
      // we don't need to send sessionId param in POST request, because we support connection with server via HttpSession
      //var sessionId = document.getElementById('sessionId').value;
      xmlhttp.send(/*'sessionId=' + sessionId*/); 
      //When readyState is 4 then get the server output
      xmlhttp.onreadystatechange = function() { 
         if (xmlhttp.readyState == 4) { 
            if (xmlhttp.status == 200) { 
               var response = xmlhttp.responseText; 
               //document.getElementById('sessionId').value = response; 
               document.getElementById('sessionIdHead').innerHTML = response; 
            } 
            else if (xmlhttp.status == 301) {
            	//window.location.replace(xmlhttp.responseText);
            	//alert("Redirect to " + xmlhttp.responseText);
            	window.location.href = xmlhttp.responseText;
            }
            else { 
               clearInterval(_refresher); 
               console.error('Something is wrong!'); 
            } 
         } 
      }; 
   };
   
   return {
            start : function(path, ajaxInterval) {
                       _refresher = setInterval(function() { _ajaxRequest(path) }, ajaxInterval); 
                    }
          };
} () ); 

        