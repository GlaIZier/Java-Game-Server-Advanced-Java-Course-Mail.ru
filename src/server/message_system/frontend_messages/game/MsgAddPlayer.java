package server.message_system.frontend_messages.game;

import server.frontend.Game;
import server.frontend.Waiting;
import server.message_system.base.Address;
import server.message_system.frontend_messages.waiting.MsgDeleteWaitingUser;
import server.users.UserSession;

public class MsgAddPlayer extends MsgToGame {
   
   private final UserSession playerToAdd;

   public MsgAddPlayer(Address from, Address to, UserSession playerToAdd) {
      super(from, to);
      this.playerToAdd = playerToAdd;
   }

   @Override
   public void exec(Game game) {
      game.addPlayer(playerToAdd);
      // send msg to waiting to delete waiting player
      game.getMessageSystem().sendMessage(new MsgDeleteWaitingUser(getTo(), game.getMessageSystem().
            getAddressService().getAddress(Waiting.class), playerToAdd.getSession() ) );
   }

}
