package main;

public class User {
   
   private final String name;
   
   private final int userID;

   public User(String name, int userID) {
      this.name = name;
      this.userID = userID;
   }

   public String getName() {
      return name;
   }

   public int getUserID() {
      return userID;
   }

}
