package server.users;

import javax.servlet.http.HttpSession;

public class UserSession implements Comparable<UserSession> {
   
   private final HttpSession session;
   
   private final User me;
   
   private int timeToFinish;
   
   private int clickedByMe;
   
   private User enemy;
   
   private int clickedByEnemy;
   
   public UserSession(HttpSession session, User user) {
      this.session = session;
      this.me = user;
   }

   public int getTimeToFinish() {
      return timeToFinish;
   }

   public void setTimeToFinish(int timeToFinish) {
      this.timeToFinish = timeToFinish;
   }

   public int getClickedByMe() {
      return clickedByMe;
   }

   public void setClickedByUser(int clickedByUser) {
      this.clickedByMe = clickedByUser;
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

   public HttpSession getSession() {
      return session;
   }

   public User getMe() {
      return me;
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
