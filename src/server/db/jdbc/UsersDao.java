package server.db.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import server.main.ServerSettings;

public class UsersDao {
   
   private final Connection connection;

   public UsersDao(Connection connection) {
      this.connection = connection;
   }
  
   public boolean createUsersTable() {
      String createQuery = "create table " + ServerSettings.DB_USERS_TABLE_NAME + " (id bigint auto_increment, name varchar(255), wins bigint, primary key (id))";
      return TExecutor.execUpdate(connection, createQuery);
   }
   
   public boolean dropUsersTable() {
      String dropQuery = "drop table if exists " + ServerSettings.DB_USERS_TABLE_NAME;
      return TExecutor.execUpdate(connection, dropQuery);
   }
   
   public boolean addNewUsers(Set<String> userNames) {
      String query = "insert into " + ServerSettings.DB_USERS_TABLE_NAME + " (name, wins) values (?, 0)";
      return TExecutor.execUpdate(connection, query, userNames);
   }
   
   public boolean addUser(String userName, int wins) {
      String query = "insert into " + ServerSettings.DB_USERS_TABLE_NAME + " (name, wins) values ('" + userName + "', " + wins + ")";
      return TExecutor.execUpdate(connection, query);
   }
   
   public boolean updateWins(String userName, int newWins) {
      String query = "update " + ServerSettings.DB_USERS_TABLE_NAME + " set wins = " + newWins + " where name = '" + userName + "'";
      return TExecutor.execUpdate(connection, query);
   }
   
   public UsersDataSet getUsersDataSet(String userName) {
      String query = "select * from " + ServerSettings.DB_USERS_TABLE_NAME + " where name = '" + userName + "'";
      
      UsersDataSet usersDataSet = TExecutor.execQuery(connection, query, new TResultHandler<UsersDataSet>() {
         
         public UsersDataSet handle(ResultSet resultSet) {
            try {
               if (resultSet.next())
                  return new UsersDataSet(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
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
