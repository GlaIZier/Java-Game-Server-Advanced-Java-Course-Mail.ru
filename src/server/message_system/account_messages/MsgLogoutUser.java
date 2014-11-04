package server.message_system.account_messages;

import server.message_system.base.Address;
import server.users.AccountService;

public class MsgLogoutUser extends MsgToAccountService{

   private final String userName;
   
   public MsgLogoutUser(Address from, Address to, String userName) {
      super(from, to);
      this.userName = userName;
   }

   @Override
   public void exec(AccountService accountService) {
      accountService.logout(userName);
   }

}
