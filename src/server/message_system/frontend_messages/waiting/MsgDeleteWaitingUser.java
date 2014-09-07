package server.message_system.frontend_messages.waiting;

import javax.servlet.http.HttpSession;

import server.frontend.Waiting;
import server.message_system.base.Address;

public class MsgDeleteWaitingUser extends MsgToWaiting {
   
   private final HttpSession session;

   public MsgDeleteWaitingUser(Address from, Address to, HttpSession session) {
      super(from, to);
      this.session = session;
   }
   
   @Override
   public void exec(Waiting waiting) {
      waiting.deleteWaitingUser(session);
   }

}
