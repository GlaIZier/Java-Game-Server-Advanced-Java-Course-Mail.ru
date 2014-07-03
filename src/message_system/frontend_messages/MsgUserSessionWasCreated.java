package message_system.frontend_messages;

import javax.servlet.http.HttpSession;
import message_system.base.Address;
import frontend.Logon;

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
