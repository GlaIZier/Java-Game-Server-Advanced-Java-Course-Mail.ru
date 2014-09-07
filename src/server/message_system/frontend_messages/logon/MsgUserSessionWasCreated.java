package server.message_system.frontend_messages.logon;

import javax.servlet.http.HttpSession;

import server.frontend.Logon;
import server.message_system.base.Address;

public class MsgUserSessionWasCreated extends MsgToLogon {
   
   private final HttpSession session;
   
   public MsgUserSessionWasCreated(Address from, Address to, HttpSession session) {
      super(from, to);
      this.session = session;
   }

   @Override
   public void exec(Logon logon) {
      logon.deleteCreatedUserSession(session);
   }

}
