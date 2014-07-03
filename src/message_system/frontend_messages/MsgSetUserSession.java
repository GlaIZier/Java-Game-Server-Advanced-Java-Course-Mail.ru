package message_system.frontend_messages;

import javax.servlet.http.HttpSession;

import users.User;
import frontend.Game;
import message_system.base.Address;

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
   }

}
