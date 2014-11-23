package server.message_system.db_messages;

import server.db.DatabaseService;
import server.message_system.base.Address;

public class MsgLogoutUser extends MsgToDatabaseService{

   private final String userName;
   
   public MsgLogoutUser(Address from, Address to, String userName) {
      super(from, to);
      this.userName = userName;
   }

   @Override
   public void exec(DatabaseService databaseService) {
      databaseService.logout(userName);
   }

}
