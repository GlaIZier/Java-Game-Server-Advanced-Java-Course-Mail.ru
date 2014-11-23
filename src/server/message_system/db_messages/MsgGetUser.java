package server.message_system.db_messages;

import javax.servlet.http.HttpSession;

import server.db.DatabaseService;
import server.frontend.Logon;
import server.frontend.Waiting;
import server.message_system.base.Address;
import server.message_system.frontend_messages.logon.MsgUnauthenticated;
import server.message_system.frontend_messages.waiting.MsgAddWaitingUser;
import server.users.User;
// import users.Session;

public class MsgGetUser extends MsgToDatabaseService{
   
   private final HttpSession session;
   
   private final String userName;

   public MsgGetUser(Address from, Address to, HttpSession session, String userName) {
      super(from, to);
      this.session = session;
      this.userName = userName;
   }

   @Override
   public void exec(DatabaseService databaseService) {
      User user = databaseService.getUser(userName);
      if (user != null) 
         databaseService.getMessageSystem().sendMessage(new MsgAddWaitingUser(getTo(), databaseService.
               getMessageSystem().getAddressService().getAddress(Waiting.class), session, user));
      else 
         databaseService.getMessageSystem().sendMessage(new MsgUnauthenticated(getTo(), databaseService.
            getMessageSystem().getAddressService().getAddress(Logon.class), session));
   }

}
