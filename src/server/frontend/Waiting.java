package server.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.game.GameMechanics;
import server.main.ServerSettings;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.message_system.game_mechanics_messages.MsgAddWaitingPlayer;
import server.resources.FrontendResource;
import server.users.User;
import server.users.UserSession;
import server.utils.Context;

public class Waiting extends HttpServlet implements Runnable, Abonent {
   
   private static final long serialVersionUID = 6011218407952120472L;
   
   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private final FrontendResource frontendResource;

   private Map<HttpSession, UserSession> waitingUsers = new HashMap<>();

   public Waiting(Context context) {
      this.messageSystem = (MessageSystem) context.getImplementation(MessageSystem.class);
      this.address = new Address();
      this.frontendResource = (FrontendResource) context.getImplementation(FrontendResource.class);
      this.messageSystem.addService(this);
   }

   @Override
   public Address getAddress() {
      return address;
   }
   
   @Override
   public void run() {
      while (true) {
         messageSystem.execWithDynamicSleepFor(this);
      }
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      includeOkInfo(response);
      HttpSession session = request.getSession(false);
      if (session == null) {
         System.out.println("No session in GET request to Waiting!");
         response.sendRedirect("");
         return;
      }
      UserSession userSession = waitingUsers.get(session);
      if (userSession == null) {
         System.out.println("Error in GET request to Waiting. UserSession hasn't been created yet!");
         response.sendRedirect("");
         return;
      }
      response.getWriter().print(getWaitingPage(userSession, session) );
      messageSystem.sendMessage(new MsgAddWaitingPlayer(getAddress(), messageSystem.
            getAddressService().getAddress(GameMechanics.class), userSession));
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      HttpSession session = request.getSession();
      if (session == null || session.isNew())
         response.sendRedirect("");
      if (waitingUsers.containsKey(session) ) {
         includeOkInfo(response);
         response.getWriter().print("AJAX: " + session.getId());
      }
      else {
         response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
         response.getWriter().print(frontendResource.getGamePath());
      }
   }  
   
   public void createUserSessionAndAddToWaiting(HttpSession httpSession, User user) {
      if ( httpSession == null || user == null )
         throw new IllegalArgumentException();
      UserSession userSession = new UserSession(httpSession, user);
      waitingUsers.put(httpSession, userSession);
   }
   
   public void deleteWaitingUser(HttpSession httpSession) {
      if (httpSession == null) 
         throw new IllegalArgumentException();
      if ( waitingUsers.remove(httpSession) == null) 
         System.out.println("No such HttpSession " + httpSession.getId() + "in waiting. Couldn't delete it");
   }
   
   private void includeOkInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
   }

   private String getWaitingPage(UserSession userSession, HttpSession session) {
      Map<String, Object> pageVars = new HashMap<>();
      Map<String, Object> userVar = new HashMap<>();
      userVar.put("name", userSession.getMe().getName());
      userVar.put("id", Integer.toString(userSession.getMe().getId() ) );
      userVar.put("wins", Integer.toString(userSession.getMe().getWins() ) );
      pageVars.put("user", userVar);
      pageVars.put("sessionId", session.getId() );
      pageVars.put("path", frontendResource.getWaitingPath());
      pageVars.put("ajaxInterval", Integer.toString(ServerSettings.FRONTEND_TICK_IN_MILLIS) );
      return PageGenerator.getPage(frontendResource.getTemplatesDir(), frontendResource.getWaitingTemplate(), pageVars);
   }
   
   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
   
}