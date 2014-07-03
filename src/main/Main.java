package main;
 
import message_system.base.MessageSystem;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import users.AccountService;
import frontend.Game;
import frontend.Logon;

public class Main {

   private static final int PORT = 8080;
   
   private static final Server SERVER = new Server(PORT);
   
   private static final String RESOURCE_FOLDER = "static";
   
   public static void main(String[] args) throws Exception {
      MessageSystem ms = new MessageSystem();
      
      Logon logon = new Logon(ms);
      Thread logonThread =  new Thread(logon);
      
      Game game = new Game(ms);
      Thread gameThread = new Thread(game);
      
      AccountService accountService = new AccountService(ms);
      Thread accountServiceThread = new Thread(accountService);
      
      logonThread.start();
      gameThread.start();
      accountServiceThread.start();
      
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.addServlet(new ServletHolder(logon), Logon.PATH);
      context.addServlet(new ServletHolder(game), Game.PATH);
      
      ResourceHandler resourceHandler = new ResourceHandler();
      resourceHandler.setDirectoriesListed(true);
      resourceHandler.setResourceBase(RESOURCE_FOLDER);
      
      
      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[] { resourceHandler, context } );
      SERVER.setHandler(handlers);
      
      SERVER.start();
      SERVER.join();
   }

   public static Server getServer() {
      return SERVER;
   }

   public static void stopServer() {
      try {
         SERVER.stop();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
