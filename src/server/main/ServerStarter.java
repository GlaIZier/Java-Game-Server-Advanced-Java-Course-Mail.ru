package server.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import server.db.DatabaseService;
import server.db.jdbc.JdbcDatabaseService;
import server.db.mock.AccountService;
import server.frontend.Game;
import server.frontend.Waiting;
import server.frontend.Logon;
import server.game.GameMechanics;
import server.message_system.base.MessageSystem;
import server.resources.FrontendResource;
import server.utils.Context;
import server.utils.Vfs;
import server.utils.XmlFileSaxReader;

public class ServerStarter {

   private static final Server SERVER = new Server(ServerSettings.PORT);

   public static void main(String[] args) {
      
      Context context = createContext();

      Logon logon = new Logon(context);
      Thread logonThread = new Thread(logon);

      Waiting waiting = new Waiting(context);
      Thread waitingThread = new Thread(waiting);

      Game game = new Game(context);
      Thread gameThread = new Thread(game);

      DatabaseService databaseService = new AccountService(context);
      Thread accountServiceThread = new Thread(databaseService);

      GameMechanics gameMechanics = new GameMechanics(context);
      Thread gameMechanicsThread = new Thread(gameMechanics);

      logonThread.start();
      waitingThread.start();
      gameThread.start();
      accountServiceThread.start();
      gameMechanicsThread.start();

      ServletContextHandler servletContextHandler = new ServletContextHandler(
            ServletContextHandler.SESSIONS);
      servletContextHandler.addServlet(new ServletHolder(logon), ((FrontendResource) context.getImplementation(FrontendResource.class)).getLogonPath());
      servletContextHandler.addServlet(new ServletHolder(waiting), ((FrontendResource) context.getImplementation(FrontendResource.class)).getWaitingPath());
      servletContextHandler.addServlet(new ServletHolder(game), ((FrontendResource) context.getImplementation(FrontendResource.class)).getGamePath());

      ResourceHandler resourceHandler = new ResourceHandler();
      resourceHandler.setDirectoriesListed(true);
      resourceHandler.setResourceBase(ServerSettings.RESOURCE_FOLDER);

      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[] { resourceHandler, servletContextHandler });
      SERVER.setHandler(handlers);

      startServer();
      if (args == null || args.length == 0 || !args[0].equals(ServerSettings.START_WITHOUT_JOIN_ARG))
         joinServer();
   }
   
   private static Context createContext() {
      Map<Class<?>, Object> servicesToImpl = new HashMap<>();
      // put MsgSystem
      servicesToImpl.put(MessageSystem.class, MessageSystem.getInstance());
//      Context context = new Context(new AbstractMap.SimpleEntry<Class<?>, Object>(
//            MessageSystem.class, MessageSystem.getInstance()));
      
      // put all resources
      Vfs vfs = new Vfs(ServerSettings.DATA_FOLDER);
      Iterator<String> resourceIterator = vfs.bfsWoStartDir("");
      while (resourceIterator.hasNext()) {
         XmlFileSaxReader fileReader = new XmlFileSaxReader(resourceIterator.next());
         servicesToImpl.put(fileReader.getInstanceClass(), fileReader.getInstance());
      }
      
      Context context = new Context(servicesToImpl);
      return context;
   }

   public static void startServer() {
      if (SERVER.isStarted() || SERVER.isStarting()) {
         System.out.println("Server is started. Can't start again!");
         return;
      }
      try {
         SERVER.start();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void joinServer() {
      try {
         SERVER.join();
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public static void stopServer() {
      if (SERVER.isStopped() || SERVER.isStopping()) {
         System.out.println("Server is stopped. Can't stop again!");
         return;
      }
      try {
         SERVER.stop();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
