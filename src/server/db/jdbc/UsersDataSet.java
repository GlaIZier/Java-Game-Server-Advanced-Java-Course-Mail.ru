package server.db.jdbc;

public class UsersDataSet {
   
   private final int id;
   
   private final String name;

   public UsersDataSet(int id, String name) {
      this.id = id;
      this.name = name;
   }
   
   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

}