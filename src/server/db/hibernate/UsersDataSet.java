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

   // need default constructor (without arguments) to hibernate read work
   public UsersDataSet() {}
   
   public UsersDataSet(int id, String name) {
      this.id = id;
      this.name = name;
   }
   
   public UsersDataSet(String name) {
      // use -1 when db need to autoincrement it
      this.id = -1;
      this.name = name;
   }
   
   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

}
