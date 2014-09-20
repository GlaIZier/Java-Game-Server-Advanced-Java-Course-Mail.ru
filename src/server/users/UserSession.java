package server.users;

import javax.servlet.http.HttpSession;

import server.game.GameResult;

public class UserSession implements Comparable<UserSession> {
   
   private final HttpSession session;
   
   private final User me;
   
   private int timeToFinish;
   
   private int clickedByMe;
   
   private User enemy;
   
   private int clickedByEnemy;
   
   private GameResult gameResult;
   
   private boolean clicksFinished;
   
   public UserSession(HttpSession session, User user) {
      this.session = session;
      this.me = user;
   }

   public int getTimeToFinish() {
      return timeToFinish;
   }

   public void setTimeToFinish(int timeToFinish) {
      if (timeToFinish <= 0) 
         throw new IllegalArgumentException();
      this.timeToFinish = timeToFinish;
   }

   public int getClickedByMe() {
      return clickedByMe;
   }

   public void setClickedByUser(int clicks) {
      if (clicks < 0) 
         throw new IllegalArgumentException();
      this.clickedByMe = clicks;
   }
   
   public void addClicksToMe(int clicks) {
      if (clicks < 0)
         throw new IllegalArgumentException();
      clickedByMe += clicks;
   }

   public User getEnemy() {
      return enemy;
   }

   public void setEnemy(User enemy) {
      this.enemy = enemy;
   }

   public int getClickedByEnemy() {
      return clickedByEnemy;
   }

   public void setClickedByEnemy(int clicks) {
      if (clicks < 0) 
         throw new IllegalArgumentException();
      this.clickedByEnemy = clicks;
   }
   
   public void addClicksToEnemy(int clicks) {
      if (clicks < 0)
         throw new IllegalArgumentException();
      clickedByEnemy += clicks;
   }

   public HttpSession getSession() {
      return session;
   }

   public User getMe() {
      return me;
   }
   
   public GameResult getGameResult() {
      return gameResult;
   }
   
   public String getGameResultMsg() {
      return gameResult.toString();
   }

   public void setGameResult(GameResult gameResult) {
      this.gameResult = gameResult;
   }
   
   public void finishClicks() {
      clicksFinished = true;
   }
   
   public boolean finishedClicks() {
      return clicksFinished;
   }

   // use as key in Set in GameMechanics
   @Override
   public int hashCode() {
      return session.getId().hashCode();
   }

   @Override
   public boolean equals(Object that) {
      if (this == that)
         return true;
      if (that == null)
         return false;
      if (getClass() != that.getClass())
         return false;
      if (!session.getId().equals( ( (UserSession) that).getSession().getId() ) )
         return false;
      return true;
   }

   @Override
   public int compareTo(UserSession that) {
      return session.getId().compareTo(that.getSession().getId() );
   }
   
}
