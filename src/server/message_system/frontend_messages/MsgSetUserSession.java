package server.message_system.frontend_messages;

import javax.servlet.http.HttpSession;

import server.frontend.Game;
import server.frontend.Logon;
import server.message_system.base.Address;
import server.users.User;

public class MsgSetUserSession extends MsgToGame {
   
   private final HttpSession session;
   
   private final User user;

   public MsgSetUserSession(Address from, Address to, HttpSession session,  User user) {
      super(from, to);
      this.session = session;
      this.user = user;
   }

   @Override
   public void exec(Game game) {
      game.setUserSession(session, user);
      // send message to logon to redirect to game
      game.getMessageSystem().sendMessage(new MsgUserSessionWasCreated(getTo(), 
            game.getMessageSystem().getAddressService().getAddress(Logon.class), session) );
   }

}
