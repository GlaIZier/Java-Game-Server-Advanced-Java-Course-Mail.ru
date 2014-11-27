package server.db.mock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import server.db.DatabaseService;
import server.main.ServerSettings;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.users.User;
import server.utils.Context;
import server.utils.TimeHelper;

/**
 * Class for emulation db work.
 * @author Mike
 *
 */
public class AccountService implements DatabaseService {
   
   private static AtomicInteger lastGeneratedUserID = new AtomicInteger();

   private final Map<String, User> userNameToUser = new HashMap<>();
   
   private final Set<String> loggedInUsers = new HashSet<>();
   
   private final Address address;
   
   private final MessageSystem messageSystem;
   
   public AccountService(Context context) {
      this.messageSystem = (MessageSystem) context.getImplementation(MessageSystem.class);
      address = new Address();
      this.messageSystem.addService(this);
   }
   
   public AccountService(MessageSystem messageSystem) {
      this.messageSystem = messageSystem;
      address = new Address();
      this.messageSystem.addService(this);
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
      
      TimeHelper.sleep(ServerSettings.DB_TEST_DELAY_IN_MILLIS );
      User user = userNameToUser.get(userName);
      if (user == null) {
         Integer userID = lastGeneratedUserID.incrementAndGet();
         user = new User(userName, userID);
         userNameToUser.put(userName, user);
      }
      loggedInUsers.add(userName);
      
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

   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
}
