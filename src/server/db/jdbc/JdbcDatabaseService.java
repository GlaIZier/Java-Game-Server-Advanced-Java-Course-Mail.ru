package server.db.jdbc;

import server.db.DatabaseService;
import server.main.ServerSettings;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.users.User;
import server.utils.Context;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JdbcDatabaseService implements DatabaseService {
   
   private final static Set<String> REGISTERED_USERS = new TreeSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", 
                                                                                 "h", "i", "j", "k", "l", "m", "n",
                                                                                 "o", "p", "q", "r", "s", "t", "u",
                                                                                 "v", "w", "x", "y", "z"));  


	private final Address address;

	private final MessageSystem messageSystem;

	private final Connection connection;
	
   // TODO make hashmap cache for often users with soft references
   private final Map<String, User> cache = new HashMap<>();
   
   private final Set<String> loggedInUsers = new HashSet<>();
  
	public JdbcDatabaseService(Context context) {
		this.messageSystem = (MessageSystem) context
				.getImplementation(MessageSystem.class);
		address = new Address();
		this.messageSystem.addService((DatabaseService) this);
		String dbUrl = recreateDb(ServerSettings.DB_SERVER_URL, ServerSettings.DB_LOGIN, 
		      ServerSettings.DB_PASSWORD, ServerSettings.DB_NAME);
      connection = connectTo(dbUrl, ServerSettings.DB_LOGIN, ServerSettings.DB_PASSWORD);
      createInitialTable(connection);
	}

   public JdbcDatabaseService(MessageSystem messageSystem) {
      this.messageSystem = messageSystem;
      address = new Address();
      this.messageSystem.addService(this);
      String dbUrl = recreateDb(ServerSettings.DB_SERVER_URL, ServerSettings.DB_LOGIN,
            ServerSettings.DB_PASSWORD, ServerSettings.DB_NAME);
      connection = connectTo(dbUrl, ServerSettings.DB_LOGIN, ServerSettings.DB_PASSWORD);
      createInitialTable(connection);
   }

	private static Connection connectTo(String dbServerUrl, String login, String password) {
		try {
			Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver")
					.newInstance();
			DriverManager.registerDriver(driver);
			return DriverManager.getConnection(dbServerUrl, login, password);
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String recreateDb(String dbServerUrl, String login, String password, String dbName) {
      Connection connection = connectTo(dbServerUrl, login, password);
      String dropDbQuery = "drop database if exists " + dbName;
      TExecutor.execUpdate(connection, dropDbQuery);
      String createDbQuery = "create database " + dbName + " default character set utf8 default collate utf8_general_ci";
      TExecutor.execUpdate(connection, createDbQuery);
      try {
         connection.close();
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      return dbServerUrl + dbName;
	}
	
	private static void createInitialTable(Connection connection) {
	   UsersDao usersDao = new UsersDao(connection);
	   usersDao.recreateUsersTable();
	   usersDao.addUsers(REGISTERED_USERS);
	}

	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	public void run() {
      while (true) {
         messageSystem.execWithDynamicSleepFor(this);
      }
	}
	
	// @Test
	public static void main(String[] args) {
	   try {
	      String dbUrl = recreateDb(ServerSettings.DB_SERVER_URL, ServerSettings.DB_LOGIN,
	            ServerSettings.DB_PASSWORD, ServerSettings.DB_NAME);
	      Connection connection = connectTo(dbUrl, ServerSettings.DB_LOGIN, ServerSettings.DB_PASSWORD);
	      System.out.append("Autocommit: " + connection.getAutoCommit() + '\n');
         System.out.append("DB name: " + connection.getMetaData().getDatabaseProductName() + '\n');
         System.out.append("DB version: " + connection.getMetaData().getDatabaseProductVersion() + '\n');
         System.out.append("Driver: " + connection.getMetaData().getDriverName() + '\n');
         System.out.append("Url: " + connection.getMetaData().getURL() + '\n');
         createInitialTable(connection);
         connection.close();
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
	}

   @Override
   public User getUser(String userName) {
      if (userName == null || userName.equals("")) 
         throw new IllegalArgumentException();
      
      if (loggedInUsers.contains(userName)) 
         return null;
      
      if (cache.containsKey(userName))
         return cache.get(userName);
      
      UsersDao usersDao = new UsersDao(connection);
      UsersDataSet usersDataSet = usersDao.getUsersDataSet(userName);
      
      loggedInUsers.add(usersDataSet.getName());
      
      return new User(usersDataSet.getName(), usersDataSet.getId());
   }

   @Override
   public void logout(String userName) {
      if (userName == null || userName.equals("")) 
         throw new IllegalArgumentException();
      
      loggedInUsers.remove(userName);
   }

   @Override
   public MessageSystem getMessageSystem() {
      return messageSystem;
   }

}
