package messages;

import users.AccountService;
import users.Session;
import users.User;

public class MsgGetUser extends MsgToAccountService{
   
   private final Session session;
   
   private final String userName;

   public MsgGetUser(Address from, Address to, Session session, String userName) {
      super(from, to);
      this.session = session;
      this.userName = userName;
   }

   @Override
   public void exec(AccountService accountService) {
      User user = accountService.getUser(userName);
      accountService.getMessageSystem().sendMessage(new MsgSetUserSession(getTo(), getFrom(), session, user));
   }

}
