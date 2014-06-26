package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameFrontEnd extends HttpServlet implements Runnable, Abonent {
   
   private static final long serialVersionUID = 6011218407952120472L;

   public static final String PATH = "/game";
   
   private static final String SESSION_ID_PARAM = "sessionID";
   
   private final MessageSystem messageSystem;
   
   private final Address address;

   public GameFrontEnd(MessageSystem messageSystem) {
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
         messageSystem.execFor(this);
         TimeHelper.sleep(TimeHelper.getServerTick() );
      }
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      // TODO here will be connectionEstablishedPage 
      System.out.println("GET method isn't supported inside GameFrontEnd");   
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      includeServiceInfo(response);
      int sessionID = parseSessionId(request);
      if (sessionID == 0) {
         System.out.println("No session in POST request in GameFrontEnd");
         return;
      }
      Session session = new Session(sessionID);
      response.getWriter().print(sessionID);
   }  
   
   private void includeServiceInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
   }
   
   private int parseSessionId(HttpServletRequest request) {
      String param = request.getParameter(SESSION_ID_PARAM);
      if (param == null) 
         return 0;
      try {
         return Integer.parseInt(param);
      }
      catch (NumberFormatException e) {
         param = param.replaceAll("\\D+","");
         return Integer.parseInt(param);
      }
   }

}
