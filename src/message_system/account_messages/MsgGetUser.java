package message_system.account_messages;

import javax.servlet.http.HttpSession;

import frontend.Game;
import message_system.base.Address;
import message_system.frontend_messages.MsgSetUserSession;
import message_system.frontend_messages.MsgUserSessionWasCreated;
import users.AccountService;
// import users.Session;
import users.User;

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
      accountService.getMessageSystem().sendMessage(new MsgSetUserSession(getTo(), accountService.
            getMessageSystem().getAddressService().getAddress(Game.class), session, user));
      accountService.getMessageSystem().sendMessage(new MsgUserSessionWasCreated(getTo(), getFrom(), session) );
   }

}
