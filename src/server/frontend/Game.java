package server.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.db.DatabaseService;
import server.game.GameMechanics;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.message_system.db_messages.MsgLogoutUser;
import server.message_system.game_mechanics_messages.MsgAddClicksTo;
import server.resources.FrontendResource;
import server.users.UserSession;
import server.utils.Context;

public class Game extends HttpServlet implements Runnable, Abonent {

   private static final long serialVersionUID = 1204679657775275767L;

   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private final FrontendResource frontendResource;
   
   private Map<HttpSession, UserSession> sessionToPlayers = new HashMap<>();
   
   public Game(Context context) {
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
         System.out.println("No session in GET request to Game!");
         response.sendRedirect("");
         return;
      }
      UserSession userSession = sessionToPlayers.get(session);
      if (userSession == null) {
         System.out.println("Error in GET request to Game. No player with " + session.getId() + " session!");
         response.sendRedirect("");
         return;
      }
      response.getWriter().print(getGamePage(userSession) );
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      HttpSession session = request.getSession();
      if (session == null || session.isNew() ) {
         System.out.println("Unknown or no session in POST request to Game!");
         response.sendRedirect("");
      }
      if (!sessionToPlayers.containsKey(session) ) {
         System.out.println("Unknown or no session in POST request to Game!");
         response.sendRedirect("");
      }
      String clicks = request.getParameter(frontendResource.getGameClicksParam());
      UserSession player = sessionToPlayers.get(session);
      if (clicks != null) {
         messageSystem.sendMessage(new MsgAddClicksTo(address, messageSystem.getAddressService()
               .getAddress(GameMechanics.class), player, Integer.parseInt(clicks) ) ); 
      }
      else {
         //check if game result is ready
         if (player.getGameResult() != null) {
            finishGame(response, player);
         }
         else {
            // Send to msg to frontend to wait results 
            response.getWriter().print(createJson(frontendResource.getGameLoadingMsg(), frontendResource.getGameLoadingMsg()));
         }
      }
   }  
   
   
   private void includeOkInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
   }
   
   private String createJson(String gameResult, String enemyClicks) {
      return "{ \"gameResult\": \"" + gameResult + "\", \"enemyClicks\": \"" + enemyClicks + "\" }";
   }
   
   private void finishGame(HttpServletResponse response, UserSession player) throws IOException {
      response.getWriter().print(createJson(player.getGameResultMsg(), 
            Integer.toString(player.getClickedByEnemy() ) ) );
      messageSystem.sendMessage(new MsgLogoutUser(address, messageSystem.getAddressService()
            .getAddress(DatabaseService.class), player.getMe().getName())); 
      deletePlayer(player);
      HttpSession playerSession = player.getSession();
      playerSession.invalidate();
   }
   
   public void addPlayer(UserSession player) {
      if (player == null)
         throw new IllegalArgumentException();
      if (sessionToPlayers.put(player.getSession(), player) != null ) 
         System.out.println("Replace existing session in Game while adding new player!");
   }
   
   public void updatePlayer(UserSession updatedPlayer) {
     if (updatedPlayer == null) 
        throw new IllegalArgumentException();
     HttpSession session = updatedPlayer.getSession();
     UserSession oldPlayer = sessionToPlayers.get(session);
     if (oldPlayer == null) 
        throw new IllegalArgumentException("Can't update player in Game. No such session: " + session);
     sessionToPlayers.put(session, updatedPlayer);
   }
   
   private void deletePlayer(UserSession player) {
      if (player == null) 
         throw new IllegalArgumentException();
      if (sessionToPlayers.remove(player.getSession() ) == null) 
         System.out.println("No such player: " + player.getSession().getId() + "in game. Couldn't delete it");
   }
   
   private String getGamePage(UserSession userSession) {
      Map<String, Object> gamePageVars = new HashMap<>();
      gamePageVars.put("userName", userSession.getMe().getName() );
      gamePageVars.put("enemyName", userSession.getEnemy().getName() );
      gamePageVars.put("gameTime", userSession.getTimeToFinish() );
      return PageGenerator.getPage(frontendResource.getTemplatesDir(), frontendResource.getGameTemplate(), gamePageVars);
   }
   
   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
   
}
