package server.users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.utils.Context;
import server.utils.TimeHelper;

public class AccountService implements Runnable, Abonent {
   
   private static AtomicInteger lastGeneratedUserID = new AtomicInteger();

   // TODO here will be DB
   // TODO make hashmap for cache for often users with soft references
   private final Map<String, User> userNameToUser = new ConcurrentHashMap<>();
   
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
   
   public User getUser(String userName) {
      TimeHelper.sleep(TimeHelper.DB_TEST_DELAY_IN_MILLIS );
      User user = userNameToUser.get(userName);
      if (user == null) {
         Integer userID = lastGeneratedUserID.incrementAndGet();
         user = new User(userName, userID);
         userNameToUser.put(userName, user);
      }
      return user;
   }

   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
}
