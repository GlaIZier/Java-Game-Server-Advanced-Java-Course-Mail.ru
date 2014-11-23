package server.db;

import server.message_system.base.Abonent;
import server.message_system.base.MessageSystem;
import server.users.User;

public interface DatabaseService extends Runnable, Abonent {
   
   public User getUser(String userName);
   
   public void logout(String userName);
   
   public MessageSystem getMessageSystem();

}
