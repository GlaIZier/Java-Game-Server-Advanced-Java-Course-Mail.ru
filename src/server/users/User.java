package server.users;

public class User {
   
   private final String name;
   
   private final int id;

   public User(String name, int userID) {
      this.name = name;
      this.id = userID;
   }

   public String getName() {
      return name;
   }

   public int getId() {
      return id;
   }

}
