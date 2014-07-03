package message_system.frontend_messages;

import message_system.base.Abonent;
import message_system.base.Address;
import message_system.base.Msg;
import frontend.Logon;

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
