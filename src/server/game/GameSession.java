package server.game;

import server.users.UserSession;

public class GameSession {

   private final UserSession first;
   
   private final UserSession second;
   
   public GameSession(UserSession first, UserSession second) {
      first.setTimeToFinish(GameMechanics.GAME_TIME_IN_SECONDS);
      second.setTimeToFinish(GameMechanics.GAME_TIME_IN_SECONDS);
      first.setEnemy(second.getMe() );
      second.setEnemy(first.getMe() );
      this.first = first;
      this.second = second;
   }
   
   

}
