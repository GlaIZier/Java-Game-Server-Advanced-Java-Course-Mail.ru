package main;

public class MsgSetUserSession extends MsgToFrontEnd {
   
   private final Session session;
   
   private final User user;
   
   public MsgSetUserSession(Address from, Address to, Session session,  User user) {
      super(from, to);
      this.session = session;
      this.user = user;
   }

   @Override
   public void exec(FrontEnd frontEnd) {
      frontEnd.setUserSession(session, user);
   }

}
