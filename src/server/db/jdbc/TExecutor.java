package server.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

public class TExecutor {
   
   public static boolean execUpdate(Connection connection, String query) {
      Statement statement = null;
      boolean resultOk = false;
      try {
         statement = connection.createStatement();
         statement.execute(query);
         statement.close();
         resultOk = true;
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      finally {
         if (statement != null)
            try {
               statement.close();
            }
            catch (SQLException e) {
               System.out.println("Statement hasn't been closed!");
               e.printStackTrace();
               resultOk = false;
            }
      }
      return resultOk;
   }

   public static boolean execUpdate(Connection connection, String[] queries) {
      Statement statement = null;
      boolean resultOk = false; 
      try {
         connection.setAutoCommit(false);
         for (String query : queries) {
            statement = connection.createStatement();
            statement.execute(query);
            statement.close();
         }
         connection.commit();
         resultOk = true;
      }
      catch (SQLException e) {
         e.printStackTrace();
         try {
            connection.rollback();
            connection.setAutoCommit(true);
         }
         catch (SQLException rollbackE) {
            rollbackE.printStackTrace();
         }
      }
      finally {
         if (statement != null)
            try {
               statement.close();
            }
            catch (SQLException e) {
               System.out.println("Statement hasn't been closed!");
               e.printStackTrace();
               resultOk = false;
            }
      }
      return resultOk;
   }
   
   public static boolean execUpdate(Connection connection, String query, Map<Integer, String> intToString) {
      PreparedStatement statement = null;
      boolean resultOk = false; 
      try {
         connection.setAutoCommit(false);
         statement = connection.prepareStatement(query);
         for (Map.Entry<Integer, String> pair : intToString.entrySet()) {
            statement.setInt(1, pair.getKey());
            statement.setString(2, pair.getValue());
            statement.executeUpdate();
         }
         connection.commit();
         statement.close();
         resultOk = true;
      }
      catch (SQLException e) {
         e.printStackTrace();
         try {
            connection.rollback();
            connection.setAutoCommit(true);
         }
         catch (SQLException rollbackE) {
            rollbackE.printStackTrace();
         }
      }
      finally {
         if (statement != null)
            try {
               statement.close();
            }
            catch (SQLException e) {
               System.out.println("Statement hasn't been closed!");
               e.printStackTrace();
               resultOk = false;
            }
      }
      return resultOk;
   }
   
   public static boolean execUpdate(Connection connection, String query, Set<String> setOfValues) {
      PreparedStatement statement = null;
      boolean resultOk = false; 
      try {
         connection.setAutoCommit(false);
         statement = connection.prepareStatement(query);
         for (String s : setOfValues) {
            statement.setString(1, s);
            statement.executeUpdate();
         }
         connection.commit();
         statement.close();
         resultOk = true;
      }
      catch (SQLException e) {
         e.printStackTrace();
         try {
            connection.rollback();
            connection.setAutoCommit(true);
         }
         catch (SQLException rollbackE) {
            rollbackE.printStackTrace();
         }
      }
      finally {
         if (statement != null)
            try {
               statement.close();
            }
            catch (SQLException e) {
               System.out.println("Statement hasn't been closed!");
               e.printStackTrace();
               resultOk = false;
            }
      }
      return resultOk;
   }
   
   public static <T> T execQuery(Connection connection, 
         String query, 
         TResultHandler<T> handler) {
      T result = null;
      Statement statement = null;
      ResultSet resultSet = null;
      try {
         statement = connection.createStatement();
         statement.execute(query);
         resultSet = statement.getResultSet();
         result = handler.handle(resultSet);
         return result;
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      finally {
         try {
            if (resultSet != null)
               resultSet.close();
            if (statement != null) 
               statement.close();
         }
         catch (SQLException unclosedE) {
            System.out.println("Couldn't close ResultSet or Statement!");
            unclosedE.printStackTrace();
         }
      }
      return result;
   }
   
}
