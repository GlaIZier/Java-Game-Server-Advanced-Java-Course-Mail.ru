package messages;

import users.AccountService;

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
