package main;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ConcurrentHashSet;

public class FrontEnd extends HttpServlet implements Runnable, Abonent {
   
   public static final String PATH = "/logon";
   
   private static final String SESSION_ID_PARAM = "sessionID";
   
   private static final String LOGIN = "login";
   
   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private AtomicInteger handleCount = new AtomicInteger();
   
   private Set<Session> userSessionsInCreation = new ConcurrentHashSet<>();
   
   private Map<Session, UserSession> sessionToUserSession = new ConcurrentHashMap<>();
   
   //private UserSession userSession = new UserSession();
   
   public FrontEnd(MessageSystem messageSystem) {
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
      handleCount.incrementAndGet();
      System.out.println(request.getMethod());
      includeServiceInfo( response);
      int sessionID = parseSessionId(request);
      Session session = new Session(sessionID);
      if (sessionID == 0)
         // first time we get sessionID and userName
         userSessionsInCreation.add(session);
      String userName = request.getParameter(LOGIN);
      if (userName != null) {      
            // if userSession creation is in process... then ask to get user from account service
            if (userSessionsInCreation.contains(session) ) {
               // wait for authentication
               response.getWriter().print(waitForAutentication(session, userName) );
               messageSystem.sendMessage(new MsgGetUser(getAddress(), messageSystem.getAddressService().getAddress(AccountService.class),
                     session, userName));
            }
            else {
               // connection established 
               response.getWriter().print(connectionEstablishedPage(session, sessionToUserSession.get(session).getUser() ) );
            }
         }
         // just save session between client and server
      else {
         response.getWriter().print(sessionID);
      }       
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
       handleCount.incrementAndGet();
       includeServiceInfo( response);
       int sessionID = parseSessionId(request);
       Session session = new Session(sessionID);
       if (sessionID == 0)
          // first time we get sessionID and userName
          userSessionsInCreation.add(session);
       String userName = request.getParameter(LOGIN);
       if (userName != null) {      
             // if userSession creation is in process... then ask to get user from account service
             if (userSessionsInCreation.contains(session) ) {
                // wait for authentication
                response.getWriter().print(waitForAutentication(session, userName) );
                messageSystem.sendMessage(new MsgGetUser(getAddress(), messageSystem.getAddressService().getAddress(AccountService.class),
                      session, userName));
             }
             else {
                // connection established 
                response.getWriter().print(connectionEstablishedPage(session, sessionToUserSession.get(session).getUser() ) );
             }
          }
          // just save session between client and server
       else {
          response.getWriter().print(sessionID);
       }
   }

// TODO: Old. Delete after frontEnd is done
//   public void handle(String target, 
//         Request baseRequest,
//	      HttpServletRequest request,
//	      HttpServletResponse response) throws IOException, ServletException {
//      
//      if (!request.getRequestURI().contains(PATH) ) 
//         return;
//      handleCount.incrementAndGet();
//      includeServiceInfo(baseRequest, response);
//	   int sessionID = parseSessionId(request);
//	   if (sessionID == 0) {
//	      // first page
//	      Session session = new Session();
//	      // begin to create user session
//	      userSessionsInCreation.add(session);
//	      response.getWriter().print(inputNamePage(session) );
//	   }
//	   else {
//	      Session session = new Session(sessionID);
//	      String userName = request.getParameter(LOGIN);
//	      System.out.println(request.getMethod());
//	      if (request.getParameter(LOGIN) != null) {      
//	         // if userSession creation is in process... then ask to get user from account service
//	         if (userSessionsInCreation.contains(session) ) {
//	            // wait for authentication
//	            response.getWriter().print(waitForAutentication(session, userName) );
//	            messageSystem.sendMessage(new MsgGetUser(getAddress(), messageSystem.getAddressService().getAddress(AccountService.class),
//	                  session, userName));
//	         }
//	         else {
//	            // connection established 
//	            response.getWriter().print(connectionEstablishedPage(session, sessionToUserSession.get(session).getUser() ) );
//	         }
//	      }
//	      // just save session between client and server
//	      else {
//	         response.getWriter().print(sessionID);
//	      }
//	   }             
//	}

   private void includeServiceInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      //baseRequest.setHandled(true);
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
   
// TODO: Old. Delete after frontEnd is don
//   private String inputNamePage(Session session) {
//      return 
//            "<body>" +
//            "<form name='input' method='post'>" +
//               "<b>Input your name: </b>" +
//               "<input type='text' name='userName' id='name'>" +
//               "<input type='hidden' name='sessionID' id='sessionID' value='" + session.getID() + "'>" +
//               "<input type='submit' value='Submit'>" +
//            "</form>" + 
//            "</body>";  
//   }
   
   private String waitForAutentication(Session session, String userName) {
      return 
          "<body>" +
          "<h1>Waiting for authentication for sessionID: " + session.getID() + " ...</h1>" +
          "<form name='input' method='post'>" +
            "<input type='hidden' name='login' value='" + userName + "'>" +
            "<input type='hidden' name='sessionID' value='" + session.getID() + "'>" +
            "<input type='submit' value='Submit' style='visibility:hidden'>" +
          "</form>" +
          "<script>" +
          "setInterval(function(){document.input.submit()}, " + TimeHelper.getFrontEndTick() + ")" +
          "</script>" +
          "</body>";   
   }
   
   private String connectionEstablishedPage(Session session, User user) {
      return 
            "<body>" +
            "<h1 id='head'>Hello, " + user.getName() + ", with userID = " + user.getUserID() + ". Your sessionID is <span id='sessionIDHead'>" + session.getID() + "</span>.</h1>" +
            "<form name='input' method='post'>" +
               "<input type='hidden' name='sessionID' id='sessionID' value='" + session.getID() + "'>" +
            "</form>" +
            "<script>" +
               // ajax test
               "function ajaxAsyncRequest(reqURL) {" +
                   //Creating a new XMLHttpRequest object
                   "var xmlhttp = new XMLHttpRequest();" + 
                   //Create a asynchronous GET request
                   "xmlhttp.open('POST', reqURL, true);" + 
                   "xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');" +        
                   "var sessionID = document.getElementById('sessionID').value;" + 
                   "xmlhttp.send('sessionID=' + sessionID);" +
                    //When readyState is 4 then get the server output
                   "xmlhttp.onreadystatechange = function() {" +
                      "if (xmlhttp.readyState == 4) {" +
                          "if (xmlhttp.status == 200) {" +
                              "var response = xmlhttp.responseText;" +
                              "document.getElementById('sessionID').value = response;" +
                              "document.getElementById('sessionIDHead').innerHTML = response;" +
                           "}" +
                           "else {" +
                              "clearInterval(refresh);" +
                              "alert('Something is wrong!');" +
                           "}" +
                       "}" +
                   "};" +  
                "}" +
              "var refresh = setInterval(function(){ajaxAsyncRequest(\"" + PATH +"\")}, " + TimeHelper.getFrontEndTick() + ")" +
            "</script>" +
         "</body>";
   }   
   
   public void setUserSession(Session session, User user) {
      if (userSessionsInCreation.remove(session) ) {
         UserSession userSession = new UserSession(session, user);
         sessionToUserSession.put(session, userSession);
      }
   }

}
    