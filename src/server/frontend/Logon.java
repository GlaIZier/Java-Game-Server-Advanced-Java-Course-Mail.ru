package server.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.util.ConcurrentHashSet;

import server.message_system.account_messages.MsgGetUser;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.users.AccountService;
import server.utils.Context;
import server.utils.TimeHelper;

public class Logon extends HttpServlet implements Runnable, Abonent {

   public static final String PATH = "/logon";
   
   private static final long serialVersionUID = 6011218407952120472L;
   
   private static final String TEMPLATE = "wait_auth.tpl";
   
   private static final String LOGIN_PARAM = "login";
   
   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private AtomicInteger handleCount = new AtomicInteger();
   
   private Set<HttpSession> userSessionsInCreation = new ConcurrentHashSet<>();
   
   public Logon(Context context) {
      this.messageSystem = (MessageSystem) context.getImplementation(MessageSystem.class);
      this.address = new Address();
      this.messageSystem.addService(this);
   }
   
   public Logon(MessageSystem messageSystem) {
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
      handleCount.incrementAndGet();
      System.out.println("GET method isn't supported inside Logon!"); 
      response.sendRedirect("");
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
         IOException {
      handleCount.incrementAndGet();
      includeOkInfo(response);
      String userName = request.getParameter(LOGIN_PARAM);
      if (userName == null) {
         System.out.println("No name in POST request to Logon!");
         response.sendRedirect("");
      }
      HttpSession session = request.getSession();
      if (session.isNew() ) {
         // first time we get sessionID and userName
         userSessionsInCreation.add(session);
         messageSystem.sendMessage(new MsgGetUser(address, messageSystem.getAddressService()
               .getAddress(AccountService.class), session, userName));
      }
      // if userSession creation is in process... then return wait page
      if (userSessionsInCreation.contains(session)) {
         response.getWriter().print(getWaitPage(session, userName));
         //response.getWriter().print(waitForAuthentication(session, userName));
      } 
      else {
         // redirect to waiting for players page
         response.sendRedirect(Waiting.PATH);
      }
      
   }
   
   public void deleteCreatedUserSession(HttpSession session) {
      if (session == null) 
         throw new java.lang.IllegalArgumentException();
      userSessionsInCreation.remove(session);
   }

   private void includeOkInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      //baseRequest.setHandled(true);
   }
   
   private String getWaitPage(HttpSession session, String userName) {
      Map<String, Object> pageVars = new HashMap<>();
      pageVars.put("sessionId", session.getId());
      pageVars.put("userName", userName);
      pageVars.put("refreshInterval", Integer.toString(TimeHelper.FRONTEND_TICK_IN_MILLIS) );
      return PageGenerator.getPage(TEMPLATE, pageVars);
   }

}
    