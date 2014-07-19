package server.message_system.frontend_messages;

import server.frontend.Logon;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.Msg;

public abstract class MsgToLogon extends Msg {

   public MsgToLogon(Address from, Address to) {
      super(from, to);
   }

   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof Logon) 
         exec((Logon) abonent);
      else 
         System.out.println("Wrong object in MsgToLogon");
   }
   
   public abstract void exec(Logon logon);
}
