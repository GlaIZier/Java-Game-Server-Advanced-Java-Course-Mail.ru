package main;
 
import org.eclipse.jetty.server.Server;

public class Main {

   private static final int PORT = 8080;
   
   private static final Server SERVER = new Server(PORT); 
   
   public static void main(String[] args) throws Exception {
      MessageSystem ms = new MessageSystem();
      
      FrontEnd frontEnd = new FrontEnd(ms);
      Thread frontEndThread =  new Thread(frontEnd);
      
      AccountService accountService = new AccountService(ms);
      Thread accountServiceThread = new Thread(accountService);
      
      frontEndThread.start();
      accountServiceThread.start();
      
      SERVER.setHandler(frontEnd);
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