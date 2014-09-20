<!DOCTYPE html>
<html>
<head>
   <title>Game</title>
   <link rel="stylesheet" href="css/game.css" />
   <script type='text/javascript' src='js/game.js'></script>
</head>
<body>
   <div id='main'>
      <div id='game'>
         <div id='greeting'>
            <h4>Click faster! You: <span id='user_name'>${userName}</span>. Enemy: ${enemyName}.</h4>
         </div>
         <div id='game_area'>
            <div id='timer_label'>
               Time till game end: <span id='timer'>${gameTime}</span> seconds.<!--5-->
            </div>
            <div id='clicks_label' class='clicks_label'>
               You clicked: <span id='clicks' class='clicks'>0</span> times.
            </div>
            <div id='click_me' class='button'>
               <input id='button_click_me' type='button' value='Click me!' onclick='Game.addClick()'>
            </div>
         </div>
      </div>   
      <div id='overlay'>
         <div id='game_result'>
            <h4 id='game_result_head'>Loading...</h4>
         </div>
         <div id='overlay_clicks_label' class='clicks_label'>
            You (${userName}) clicked: <span id='overlay_clicks' class='clicks'></span> times.
         </div>
         <div id='overlay_enemy_clicks_label'>
            Your enemy (${enemyName}) clicked: <span id='overlay_enemy_clicks'>loading...</span> times.
         </div>
          <div id='close' class='button'>
            <form name='input' action='/logon' method='POST'>
               <input type='hidden' name='login' id='login' value='${userName}'/>
               <input id='button_next_game' type='submit' value='Next game'>
            </form>   
            <a id='close_link' href='/'>
               <input id='button_logout' type='button' value='Log out'>
            </a>               
          </div>
      </div>
   </div>
</body>
</html>
   