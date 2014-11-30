package server.db.jdbc;

public class UsersDataSet {
   
   private final int id;
   
   private final String name;
   
   private final int wins;

   public UsersDataSet(int id, String name, int wins) {
      this.id = id;
      this.name = name;
      this.wins = wins;
   }
   
   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }
   
   public int getWins() {
      return wins;
   }

}
