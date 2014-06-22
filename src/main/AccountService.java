package main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountService implements Runnable, Abonent {
   
   private static AtomicInteger lastGeneratedUserID = new AtomicInteger();

   // TODO here will be DB
   private final Map<String, User> userNameToUser = new ConcurrentHashMap<>();
   
   private final Address address;
   
   private final MessageSystem messageSystem;
   
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
         TimeHelper.sleep(TimeHelper.getDBTestDelay() );
         messageSystem.execFor(this);
         TimeHelper.sleep(TimeHelper.getServerTick() );
      }
   }
   
   public User getUser(String userName) {
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
