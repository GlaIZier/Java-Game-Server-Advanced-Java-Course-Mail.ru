package server.users;

import java.util.concurrent.atomic.AtomicInteger;

public class Session {

   private static AtomicInteger lastGeneratedID = new AtomicInteger();
   
   private final int id;
   
   public Session(int id) {
      if (id <= 0) 
         this.id = lastGeneratedID.incrementAndGet();
      else 
         this.id = id;
   }

   public Session() {
      this.id = lastGeneratedID.incrementAndGet();
   }

   public int getID() {
      return id;
   }
   
   // use session as key in UserSession collection 
   @Override
   public int hashCode() {
      return id;
   }
   
   // use for method contains in FrontEnd in userSessionsInCreation object
   @Override
   public boolean equals(Object that) {
      if (this == that)
         return true;
      if (that == null)
         return false;
      if (getClass() != that.getClass() )
         return false;
      if (id != ( (Session) that).getID() )
         return false;
      return true;
   }

}
