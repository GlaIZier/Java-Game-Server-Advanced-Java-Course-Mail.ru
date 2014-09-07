package server.main;
 
import java.util.AbstractMap;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import server.frontend.Game;
import server.frontend.Waiting;
import server.frontend.Logon;
import server.game.GameMechanics;
import server.message_system.base.MessageSystem;
import server.users.AccountService;
import server.utils.Context;

public class Main {

   public static final String START_WITHOUT_JOIN_ARG = "join_server=false";
   
   private static final int PORT = 8080;
   
   private static final Server SERVER = new Server(PORT);
   
   private static final String RESOURCE_FOLDER = "static";
   
   public static void main(String[] args) {
      
      Context context = new Context(new AbstractMap.SimpleEntry<Class<?>, Object>(
            MessageSystem.class, MessageSystem.getInstance() ) );
      
      Logon logon = new Logon(context);
      Thread logonThread =  new Thread(logon);
      
      Waiting waiting = new Waiting(context);
      Thread waitingThread = new Thread(waiting);
      
      Game game = new Game(context);
      Thread gameThread = new Thread(game);
      
      AccountService accountService = new AccountService(context);
      Thread accountServiceThread = new Thread(accountService);
      
      GameMechanics gameMechanics = new GameMechanics(context);
      Thread gameMechanicsThread = new Thread(gameMechanics);
      
      logonThread.start();
      waitingThread.start();
      gameThread.start();
      accountServiceThread.start();
      gameMechanicsThread.start();
      
      ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
      servletContextHandler.addServlet(new ServletHolder(logon), Logon.PATH);
      servletContextHandler.addServlet(new ServletHolder(waiting), Waiting.PATH);
      servletContextHandler.addServlet(new ServletHolder(game), Game.PATH);
      
      ResourceHandler resourceHandler = new ResourceHandler();
      resourceHandler.setDirectoriesListed(true);
      resourceHandler.setResourceBase(RESOURCE_FOLDER);
      
      
      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[] { resourceHandler, servletContextHandler } );
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
