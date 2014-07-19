package server.message_system.account_messages;

import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.Msg;
import server.users.AccountService;

public abstract class MsgToAccountService extends Msg{

   public MsgToAccountService(Address from, Address to) {
      super(from, to);
   }

   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof AccountService) 
         exec((AccountService) abonent);
      else 
         System.out.println("Wrong object in MsgToAccountService");
   }
   
   public abstract void exec(AccountService accountService);

}
