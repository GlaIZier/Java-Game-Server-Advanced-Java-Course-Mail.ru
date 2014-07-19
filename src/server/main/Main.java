package server.main;
 
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import server.frontend.Game;
import server.frontend.Logon;
import server.message_system.base.MessageSystem;
import server.users.AccountService;

public class Main {

   public static final String START_WITHOUT_JOIN_ARG = "join_server=false";
   
   private static final int PORT = 8080;
   
   private static final Server SERVER = new Server(PORT);
   
   private static final String RESOURCE_FOLDER = "static";
   
   public static void main(String[] args) {
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
      
      startServer();
      if (args == null || args.length == 0 || !args[0].equals(START_WITHOUT_JOIN_ARG) )
         joinServer();
   }
   
   public static void startServer() {
      if (SERVER.isStarted() || SERVER.isStarting() ) {
         System.out.println("Server is started. Can't start again!");
         return;
      }
      try {
         SERVER.start();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void joinServer() {
      try {
         SERVER.join();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public static void stopServer() {
      if (SERVER.isStopped() || SERVER.isStopping() ) {
         System.out.println("Server is stopped. Can't stop again!");
         return;
      }
      try {
         SERVER.stop();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
