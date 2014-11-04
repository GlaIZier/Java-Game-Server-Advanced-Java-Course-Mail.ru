package server.message_system.frontend_messages.logon;

import javax.servlet.http.HttpSession;

import server.frontend.Logon;
import server.message_system.base.Address;

public class MsgUnauthenticated extends MsgToLogon {
   
   private final HttpSession unauthenticatedSession;

   public MsgUnauthenticated(Address from, Address to, HttpSession unauthenticatedSession) {
      super(from, to);
      this.unauthenticatedSession = unauthenticatedSession;
   }

   @Override
   public void exec(Logon logon) {
      logon.unauthenticate(unauthenticatedSession);
   }

}
