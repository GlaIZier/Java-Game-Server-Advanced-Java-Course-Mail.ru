
window.onload = function() {
   Game.startGame(document.getElementById('timer').innerHTML);
}

var Game = Game || (function() {

   var _clicks = 0;
   
   var _remaining ;
   
   var _timer;
   
   var _tick = function(seconds) {
      _remaining -= seconds;
      document.getElementById('timer').innerHTML =  _remaining;
      if (_remaining == 0) {
         _gameOver();
      }
   };
   
   var _gameOver = function() {
      clearInterval(_timer);
      document.getElementById('button_click_me').disabled = true;
      document.getElementById('button_close').disabled = true;
      document.getElementById('game').style.visibility = "hidden";
      document.getElementById('overlay').style.visibility ="visible";
      document.getElementById('overlay_clicks').innerHTML = _clicks;
      var fakeResponse = "{ \"gameResult\": \"Loading...\", \"enemyClicks\": \"Loading...\" }";
      alert("json: " + fakeResponse);
      var json = JSON.parse(fakeResponse);
      alert(json);
      document.getElementById('game_result_head').innerHTML = json.gameResult;
      document.getElementById('overlay_enemy_clicks').innerHTML = json.enemyClicks;
      alert(json.gameResult + json.enemyClicks);
      //_sendClicks("/game");
      //_getResults("/game");
   };
   
   var _sendClicks = function(toUrl) {
      var xmlhttp = new XMLHttpRequest();
      xmlhttp.open('POST', toUrl, true);
      xmlhttp.send("clicks=" + _clicks); 
      xmlhttp.onreadystatechange = function() {
         if (xmlhttp.readyState == 4) { 
            if (xmlhttp.status == 200) { 
               console.log("Sending clicks... OK!");
            }
            else { 
               console.error('Something went wrong while sending clicks!'); 
            }
         }
      };
   }
   
   var _getResults = function(fromUrl) {
      var getResultsTimer = setInterval(function() {
         var xmlhttp = new XMLHttpRequest();
         xmlhttp.open('POST', fromUrl, true);
         xmlhttp.send(); 
         xmlhttp.onreadystatechange = function() { 
            if (xmlhttp.readyState == 4) { 
               if (xmlhttp.status == 200) {
                  var fakeResponse = "{ gameResult:'Loading...', enemyClicks: 'Loading...' }"  ;
                  var json = JSON.parse(fakeResponse);
                  if (_writeResults(json) ) {
                     document.getElementById('button_close').disabled = false;
                     clearInterval(getResultsTimer);
                  }
               }
               else { 
                  clearInterval(getResultsTimer);
                  console.error('Something is wrong!'); 
               }
            }
         };
      },
      1000);
   };
   
   // return true if server responded with results
   var _writeResults = function(json) {
      document.getElementById('game_result_head').innerHTML = json.gameResult;
      document.getElementById('overlay_enemy_clicks').innerHTML = json.enemyClicks;
      if (json.gameResult.indexOf("Loading") > -1  && json.enemyClicks.indexOf("Loading") > -1)
         return false;
      else 
         return true;
   }
   
   return {
      
      addClick: function() {
         document.getElementById('clicks').innerHTML =  ++_clicks;
      },
      
      startGame: function(remaining) {
         _remaining = remaining;
         document.getElementById('timer').innerHTML = remaining;
         _timer = setInterval(function() { _tick(1) }, 1000);
      },
      
   };
   
} () );