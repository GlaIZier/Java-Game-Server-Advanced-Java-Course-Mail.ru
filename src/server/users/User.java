package server.users;

public class User {
   
   private final String name;
   
   private final int id;
   
   private int wins;

   public User(String name, int userId) {
      this.name = name;
      this.id = userId;
      this.wins = 0;
   }
   
   public User(String name, int userId, int wins) {
      this.name = name;
      this.id = userId;
      this.wins = wins;
   }

   public String getName() {
      return name;
   }

   public int getId() {
      return id;
   }
   
   public int getWins() {
      return wins;
   }
   
   public void addWins(int wins) {
      if (wins <= 0) {
         System.out.println("Trying to add incorrect number of wins: " + wins + " for user " + name + " with id " + id);
         return;
      }
      this.wins += wins;
   }

}
