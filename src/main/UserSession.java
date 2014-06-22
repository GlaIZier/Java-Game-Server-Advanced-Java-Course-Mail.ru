package main;

public class UserSession {
   
   private final Session session;
   
   private final User user;
   
   private int timeToFinish;
   
   private int clickedByUser;
   
   private User enemy;
   
   private int clickedByEnemy;
   
   public UserSession(Session session, User user) {
      this.session = session;
      this.user = user;
   }

   public int getTimeToFinish() {
      return timeToFinish;
   }

   public void setTimeToFinish(int timeToFinish) {
      this.timeToFinish = timeToFinish;
   }

   public int getClickedByUser() {
      return clickedByUser;
   }

   public void setClickedByUser(int clickedByUser) {
      this.clickedByUser = clickedByUser;
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

   public void setClickedByEnemy(int clickedByEnemy) {
      this.clickedByEnemy = clickedByEnemy;
   }

   public Session getSession() {
      return session;
   }

   public User getUser() {
      return user;
   }

   
}
