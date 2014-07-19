package server.frontend;

import java.io.IOException;
import java.util.HashMap;
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
import server.users.User;
import server.users.UserSession;
import server.utils.TimeHelper;

public class Game extends HttpServlet implements Runnable, Abonent {
   
   public static final String PATH = "/game";
   
   private static final long serialVersionUID = 6011218407952120472L;
   
   private static final String TEMPLATE = "connection_established.tpl";
   
   private final MessageSystem messageSystem;
   
   private final Address address;

   private Map<HttpSession, UserSession> sessionToUserSession = new ConcurrentHashMap<>();

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
         messageSystem.execFor(this);
         TimeHelper.sleep(TimeHelper.SERVER_TICK_IN_MILLIS );
      }
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      includeServiceInfo(response);
      HttpSession session = request.getSession(false);
      if (session == null) {
         System.out.println("No session in GET request to Game!");
         response.sendRedirect("");
         return;
      }
      UserSession userSession = sessionToUserSession.get(session);
      if (userSession == null) {
         System.out.println("Error in GET request to Game. UserSession hasn't been created yet!");
         response.sendRedirect("");
         return;
      }
      // return connection established page
      response.getWriter().print(getConnectionEstablishedPage(userSession, session) );
//      response.getWriter().print(
//            connectionEstablishedPage(session, userSession.getUser()));
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      includeServiceInfo(response);
      HttpSession session = request.getSession();
      if (session.isNew()) throw new java.lang.IllegalStateException();
      response.getWriter().print("AJAX: " + session.getId());
   }  
   
   public void setUserSession(HttpSession session, User user) {
      if ( session == null || user == null )
         throw new IllegalArgumentException();
      UserSession userSession = new UserSession(session, user);
      sessionToUserSession.put(session, userSession);
   }
   
   private void includeServiceInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
   }

   private String getConnectionEstablishedPage(UserSession userSession, HttpSession session) {
      Map<String, Object> pageVars = new HashMap<>();
      Map<String, Object> userVar = new HashMap<>();
      userVar.put("name", userSession.getUser().getName());
      userVar.put("id", Integer.toString(userSession.getUser().getId() ) );
      pageVars.put("user", userVar);
      pageVars.put("sessionId", session.getId() );
      pageVars.put("path", PATH);
      pageVars.put("ajaxInterval", Integer.toString(TimeHelper.FRONTEND_TICK_IN_MILLIS) );
      return PageGenerator.getPage(TEMPLATE, pageVars);
   }
   
   public MessageSystem getMessageSystem() {
      return messageSystem;
   }
}


// TODO delete after frontend is done
//private String connectionEstablishedPage(HttpSession session, User user) {
//   return 
//         "<body>" +
//         "<h1 id='head'>Hello, " + user.getName() + ", with userID = " + user.getId() + ". Your sessionID is <span id='sessionIDHead'>" + session.getId() + "</span>.</h1>" +
//         "<form name='input' action='game' method='POST'>" +
//            "<input type='hidden' name='sessionID' id='sessionID' value='" + session.getId() + "'>" +
//         "</form>" +
//         "<script>" +
//            // ajax test
//            "function ajaxAsyncRequest(reqURL) {" +
//                //Creating a new XMLHttpRequest object
//                "var xmlhttp = new XMLHttpRequest();" + 
//                //Create a asynchronous GET request
//                "xmlhttp.open('POST', reqURL, true);" + 
//                "xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');" +        
//                "var sessionID = document.getElementById('sessionID').value;" + 
//                "xmlhttp.send('sessionID=' + sessionID);" +
//                 //When readyState is 4 then get the server output
//                "xmlhttp.onreadystatechange = function() {" +
//                   "if (xmlhttp.readyState == 4) {" +
//                       "if (xmlhttp.status == 200) {" +
//                           "var response = xmlhttp.responseText;" +
//                           "document.getElementById('sessionID').value = response;" +
//                           "document.getElementById('sessionIDHead').innerHTML = response;" +
//                        "}" +
//                        "else {" +
//                           "clearInterval(refresh);" +
//                           "alert('Something is wrong!');" +
//                        "}" +
//                    "}" +
//                "};" +  
//             "}" +
//           "var refresh = setInterval(function(){ajaxAsyncRequest(\"" + Game.PATH +"\")}, " + TimeHelper.getFrontEndTick() + ")" +
//         "</script>" +
//      "</body>";
//}   