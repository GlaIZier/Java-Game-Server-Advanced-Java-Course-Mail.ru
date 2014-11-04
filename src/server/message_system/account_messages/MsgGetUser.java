package server.message_system.account_messages;

import javax.servlet.http.HttpSession;

import server.frontend.Logon;
import server.frontend.Waiting;
import server.message_system.base.Address;
import server.message_system.frontend_messages.logon.MsgUnauthenticated;
import server.message_system.frontend_messages.waiting.MsgAddWaitingUser;
import server.users.AccountService;
import server.users.User;
// import users.Session;

public class MsgGetUser extends MsgToAccountService{
   
   private final HttpSession session;
   
   private final String userName;

   public MsgGetUser(Address from, Address to, HttpSession session, String userName) {
      super(from, to);
      this.session = session;
      this.userName = userName;
   }

   @Override
   public void exec(AccountService accountService) {
      User user = accountService.getUser(userName);
      if (user != null) 
         accountService.getMessageSystem().sendMessage(new MsgAddWaitingUser(getTo(), accountService.
               getMessageSystem().getAddressService().getAddress(Waiting.class), session, user));
      else 
         accountService.getMessageSystem().sendMessage(new MsgUnauthenticated(getTo(), accountService.
            getMessageSystem().getAddressService().getAddress(Logon.class), session));
   }

}
