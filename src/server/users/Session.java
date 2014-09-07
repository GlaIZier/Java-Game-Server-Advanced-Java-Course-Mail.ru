package server.users;

import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class Session {

   @Deprecated
   private static AtomicInteger lastGeneratedID = new AtomicInteger();
   
   @Deprecated
   private final int id;
   
   @Deprecated
   public Session(int id) {
      if (id <= 0) 
         this.id = lastGeneratedID.incrementAndGet();
      else 
         this.id = id;
   }

   @Deprecated
   public Session() {
      this.id = lastGeneratedID.incrementAndGet();
   }

   @Deprecated
   public int getID() {
      return id;
   }
   
   // use session as key in UserSession collection 
   @Deprecated
   @Override
   public int hashCode() {
      return id;
   }
   
   // use for method contains in FrontEnd in userSessionsInCreation object
   @Deprecated
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
