package server.message_system.frontend_messages;

import server.frontend.Game;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.Msg;

public abstract class MsgToGame extends Msg {

   public MsgToGame(Address from, Address to) {
      super(from, to);

   }
   
   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof Game) 
         exec((Game) abonent);
      else 
         System.out.println("Wrong object in MsgToFrontEnd");
   }
   
   public abstract void exec(Game game);

}
