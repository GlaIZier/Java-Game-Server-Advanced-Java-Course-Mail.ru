package server.message_system.frontend_messages.game;

import server.frontend.Game;
import server.message_system.base.Address;
import server.users.UserSession;

public class MsgSendUpdatedPlayer extends MsgToGame {
   
   private final UserSession updatedPlayer;

   public MsgSendUpdatedPlayer(Address from, Address to, UserSession updatedPlayer) {
      super(from, to);
      this.updatedPlayer = updatedPlayer;
   }

   @Override
   public void exec(Game game) {
      game.updatePlayer(updatedPlayer);
   }

}
