package server.message_system.game_mechanics_messages;

import server.game.GameMechanics;
import server.message_system.base.Address;
import server.users.UserSession;

public class MsgAddClicksTo extends MsgToGameMechanics {
   
   private final UserSession player;
   
   private final int clicks;

   public MsgAddClicksTo(Address from, Address to, UserSession player, int clicks) {
      super(from, to);
      this.player = player;
      this.clicks = clicks;
   }

   @Override
   public void exec(GameMechanics gameMechanics) {
      gameMechanics.addPlayersClicks(player, clicks);
   }

}
