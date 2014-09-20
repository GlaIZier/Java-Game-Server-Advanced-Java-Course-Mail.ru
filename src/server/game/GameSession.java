package server.game;

import server.users.UserSession;

public class GameSession {

   private final UserSession first;
   
   private final UserSession second;
   
   private boolean gameFinished;

   public GameSession(UserSession first, UserSession second) {
      this.first = first;
      this.second = second;
   }
   
   public UserSession getEnemyOf(UserSession player) {
      if (player.equals(first) )
         return second;
      else 
         return first;
   }
   
   public void finishGame() {
      gameFinished = true;
   }
   
   public boolean isGameFinished() {
      return gameFinished;
   }
}