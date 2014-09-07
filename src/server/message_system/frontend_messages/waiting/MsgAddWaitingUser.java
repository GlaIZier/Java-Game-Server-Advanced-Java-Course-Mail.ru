package server.message_system.frontend_messages.waiting;

import javax.servlet.http.HttpSession;

import server.frontend.Waiting;
import server.frontend.Logon;
import server.message_system.base.Address;
import server.message_system.frontend_messages.logon.MsgUserSessionWasCreated;
import server.users.User;

public class MsgAddWaitingUser extends MsgToWaiting {
   
   private final HttpSession session;
   
   private final User user;

   public MsgAddWaitingUser(Address from, Address to, HttpSession session,  User user) {
      super(from, to);
      this.session = session;
      this.user = user;
   }

   @Override
   public void exec(Waiting waiting) {
      waiting.addWaitingUser(session, user);
      // send message to logon to redirect to waiting
      waiting.getMessageSystem().sendMessage(new MsgUserSessionWasCreated(getTo(), 
            waiting.getMessageSystem().getAddressService().getAddress(Logon.class), session) );
   }

}
