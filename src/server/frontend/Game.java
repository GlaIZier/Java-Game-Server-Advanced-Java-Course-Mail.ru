package server.frontend;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.users.UserSession;
import server.utils.Context;

public class Game extends HttpServlet implements Runnable, Abonent {

   public static final String PATH = "/game";
   
   private static final long serialVersionUID = 1204679657775275767L;
   
   private static final String TEMPLATE = "game.tpl";
   
   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private Map<HttpSession, UserSession> sessionToPlayers = new ConcurrentHashMap<>();
   
   public Game(Context context) {
      this.messageSystem = (MessageSystem) context.getImplementation(MessageSystem.class);
      this.address = new Address();
      this.messageSystem.addService(this);
   }
   
   public Game(MessageSystem messageSystem) {
      this.messageSystem = messageSystem;
      this.address = new Address();
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
      for (Map.Entry<HttpSession, UserSession> sessionToPlayer : sessionToPlayers.entrySet() ) {
         response.getWriter().print("Hello, " + sessionToPlayer.getKey() );
      }
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
   }  
   
   private void includeOkInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
   }
   
   public void addPlayer(UserSession player) {
      if (player == null)
         throw new IllegalArgumentException();
      if (sessionToPlayers.put(player.getSession(), player) != null ) 
         System.out.println("Replace existing session in Game while adding new player!");
   }
   
   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
   
}
