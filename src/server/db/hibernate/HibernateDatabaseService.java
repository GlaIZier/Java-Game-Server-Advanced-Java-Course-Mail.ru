package server.db.hibernate;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import server.db.DatabaseService;
import server.main.ServerSettings;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.users.User;
import server.utils.Context;

public class HibernateDatabaseService implements DatabaseService {
   
   private final Address address;

   private final MessageSystem messageSystem;
   
   private final SessionFactory sessionFactory;
   
   // use SoftReference to store cache. If we need memory GC will free it.
   private SoftReference<Map<String, User>> cache = new SoftReference<Map<String, User>>(new HashMap<String, User>());
   
   private final Set<String> loggedInUsers = new HashSet<>();

   public HibernateDatabaseService(Context context) {
      this.messageSystem = (MessageSystem) context
            .getImplementation(MessageSystem.class);
      address = new Address();
      this.messageSystem.addService((DatabaseService) this);

      this.sessionFactory = createSessionFactory(createConfig());
   }

   public HibernateDatabaseService(MessageSystem messageSystem) {
      this.messageSystem = messageSystem;
      address = new Address();
      this.messageSystem.addService(this);
      
      this.sessionFactory = createSessionFactory(createConfig());
   }

   private static Configuration createConfig() {
      Configuration config = new Configuration();
      config.addAnnotatedClass(UsersDataSet.class);
      
      config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
      config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
      config.setProperty("hibernate.connection.url", ServerSettings.DB_SERVER_URL + ServerSettings.DB_NAME);
      config.setProperty("hibernate.connection.username", ServerSettings.DB_LOGIN);
      config.setProperty("hibernate.connection.password", ServerSettings.DB_PASSWORD);
      config.setProperty("hibernate.show_sql", "true");
      config.setProperty("hibernate.hbm2ddl.auto", "update");
      
      return config;
   }
   
   private static SessionFactory createSessionFactory(Configuration config) {
      StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
      builder.applySettings(config.getProperties());
      
      ServiceRegistry serviceRegistry = builder.build();  
      SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
      
      return sessionFactory;
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

   @Override
   public User getUser(String userName) {
      if (userName == null || userName.equals("")) 
         throw new IllegalArgumentException();
      
      if (loggedInUsers.contains(userName)) 
         return null;
      
      Map<String, User> strongCache = cache.get();
      if (strongCache != null && strongCache.containsKey(userName)) {
         loggedInUsers.add(userName);
         return strongCache.get(userName);
      }
      
      UsersDao usersDao = new UsersDao(sessionFactory);
      UsersDataSet usersDataSet = usersDao.read(userName);
      if (usersDataSet == null) {
         // register new user
         usersDao.save(new UsersDataSet(userName));
         usersDataSet = usersDao.read(userName);
      }
      
      loggedInUsers.add(usersDataSet.getName());
      User user = new User(usersDataSet.getName(), usersDataSet.getId(), usersDataSet.getWins());
      if (strongCache == null) {
         strongCache = new HashMap<>();
         strongCache.put(user.getName(), user);
         cache = new SoftReference<Map<String,User>>(strongCache);
      }
      else 
        cache.get().put(userName, user);
      
      return user;
   }

   @Override
   public void logout(String userName) {
      if (userName == null || userName.equals("")) 
         throw new IllegalArgumentException();
      
      if (!loggedInUsers.contains(userName))
         throw new IllegalStateException();
      
      loggedInUsers.remove(userName);
   }
   
   @Override
   public void addWins(String userName, int wins) {
      if (userName == null || userName.equals(""))
         throw new IllegalArgumentException();
      
      if (wins <= 0) {
         System.out.println("Trying to add incorrect number of wins " + wins + " for user " + userName);
         return;
      }
      
      UsersDao usersDao = new UsersDao(sessionFactory);
      UsersDataSet usersDataSet = usersDao.read(userName);
      if (usersDataSet == null) {
         System.out.println("Unknown user name: " + userName + " while trying to add userName");
         return;
      }
      
      usersDao.updateWins(userName, usersDataSet.getWins() + wins);
      
      Map<String, User> strongCache = cache.get();
      if (strongCache != null) 
         strongCache.get(userName).addWins(wins);
   }

   @Override
   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
   
   //@Test
   public static void main(String[] args) {
      SessionFactory sessionFactory = createSessionFactory(createConfig());
      Session session = sessionFactory.openSession();
      Transaction transaction = session.beginTransaction();
      System.out.append(transaction.getLocalStatus().toString() + '\n');
      session.close();
      
      UsersDataSet uds = new UsersDao(sessionFactory).read(22);
      System.out.println(uds.getId() + " " + uds.getName());
      
      uds = new UsersDao(sessionFactory).read("q");
      System.out.println(uds.getId() + " " + uds.getName());
      
//      uds = new UsersDataSet("hh");
//      new UsersDao(sessionFactory).save(uds);
      
      uds = new UsersDao(sessionFactory).read("hh");
      System.out.println(uds.getId() + " " + uds.getName());
      
      sessionFactory.close();
      System.out.println(sessionFactory.isClosed());
   }
   
}
