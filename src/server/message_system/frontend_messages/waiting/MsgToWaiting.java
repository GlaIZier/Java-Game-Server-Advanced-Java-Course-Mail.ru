package server.message_system.frontend_messages.waiting;

import server.frontend.Waiting;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.Msg;

public abstract class MsgToWaiting extends Msg {

   public MsgToWaiting(Address from, Address to) {
      super(from, to);

   }
   
   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof Waiting) 
         exec((Waiting) abonent);
      else 
         System.out.println("Wrong object in MsgToWaiting");
   }
   
   public abstract void exec(Waiting waiting);

}
