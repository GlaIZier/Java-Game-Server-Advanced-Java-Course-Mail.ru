package server.db.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class UsersDao {
   
   private final Connection connection;

   public UsersDao(Connection connection) {
      this.connection = connection;
   }
  
   public boolean recreateUsersTable() {
      String dropQuery = "drop table if exists users";
      TExecutor.execUpdate(connection, dropQuery);
      String createQuery = "create table users (id bigint auto_increment, name varchar(255), primary key (id))";
      return TExecutor.execUpdate(connection, createQuery);
   }
   
   public boolean addUsers(Set<String> userNames) {
      String query = "insert into users (name) values (?)";
      return TExecutor.execUpdate(connection, query, userNames);
   }
   
   public UsersDataSet getUsersDataSet(String userName) {
      String query = "select * from users where name=" + userName;
      
      UsersDataSet usersDataSet = TExecutor.execQuery(connection, query, new TResultHandler<UsersDataSet>() {
         
         public UsersDataSet handle(ResultSet resultSet) {
            try {
               resultSet.first();
               return new UsersDataSet(resultSet.getInt(1), resultSet.getString(2));
            }
            catch (SQLException e) {
               e.printStackTrace();
            }
            return null;
         }
      
      });
      
      return usersDataSet;
   }
   

}
