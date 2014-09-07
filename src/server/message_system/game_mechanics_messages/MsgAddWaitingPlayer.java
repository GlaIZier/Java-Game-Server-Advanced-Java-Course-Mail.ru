package server.message_system.game_mechanics_messages;

import server.game.GameMechanics;
import server.message_system.base.Address;
import server.users.UserSession;

public class MsgAddWaitingPlayer extends MsgToGameMechanics {
   
   private final UserSession userSession;

   public MsgAddWaitingPlayer(Address from, Address to, UserSession userSession) {
      super(from, to);
      this.userSession = userSession;
   }

   @Override
   public void exec(GameMechanics gameMechanics) {
      gameMechanics.addWaitingPlayer(userSession);
   }
   
}
