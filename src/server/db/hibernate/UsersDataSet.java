package server.db.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UsersDataSet {

   @Id
   @Column(name = "id")
   // for auto increment
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
   
   @Column(name = "name")
   private String name;
   
   @Column(name = "wins")
   private int wins;

   // need default constructor (without arguments) to hibernate read work
   public UsersDataSet() {}
   
   public UsersDataSet(int id, String name, int wins) {
      this.id = id;
      this.name = name;
      this.wins = wins;
   }
   
   public UsersDataSet(String name) {
      // use -1 when db need to autoincrement it
      this.id = -1;
      this.name = name;
      this.wins = 0;
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
   
   public void setWins(int wins) {
      if (wins <= 0) {
         System.out.println("Trying to add incorrect number of wins " + wins + " for user " + name + " with id " + id);
         return;
      }
      this.wins = wins;
   }

}
