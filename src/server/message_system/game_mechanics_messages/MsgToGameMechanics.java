package server.message_system.game_mechanics_messages;

import server.game.GameMechanics;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.Msg;

public abstract class MsgToGameMechanics extends Msg {

   public MsgToGameMechanics(Address from, Address to) {
      super(from, to);
   }

   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof GameMechanics) 
         exec((GameMechanics) abonent);
      else 
         System.out.println("Wrong class of object in MsgToGameMechanics: " + abonent.getClass().getName() );
   }

   
   public abstract void exec(GameMechanics gameMechanics);
   
}
