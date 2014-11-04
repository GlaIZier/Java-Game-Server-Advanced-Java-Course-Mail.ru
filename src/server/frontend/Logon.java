package server.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.util.ConcurrentHashSet;

import server.main.ServerSettings;
import server.message_system.account_messages.MsgGetUser;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.resources.FrontendResource;
import server.users.AccountService;
import server.utils.Context;

public class Logon extends HttpServlet implements Runnable, Abonent {

   private static final long serialVersionUID = 6011218407952120472L;

   private final MessageSystem messageSystem;

   private final Address address;
   
   private final FrontendResource frontendResource;

   private final AtomicInteger handleCount = new AtomicInteger();

   private final Set<HttpSession> userSessionsInCreation = new ConcurrentHashSet<>();
   
   private final Set<HttpSession> unauthenticated = new HashSet<>();

   public Logon(Context context) {
      this.messageSystem = (MessageSystem) context.getImplementation(MessageSystem.class);
      this.address = new Address();
      this.frontendResource = (FrontendResource) context.getImplementation(FrontendResource.class);
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
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      handleCount.incrementAndGet();
      System.out.println("GET method isn't supported inside Logon!");
      response.sendRedirect("");
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      handleCount.incrementAndGet();
      includeOkInfo(response);
      String userName = request.getParameter(frontendResource.getLogonLoginParam());
      if (userName == null) {
         System.out.println("No name in POST request to Logon!");
         response.sendRedirect("");
         return;
      }
      HttpSession session = request.getSession();
      if (unauthenticated.contains(session)) {
         unauthenticated.remove(session);
         System.out.println("Login failed. User with this name has been already logged in!");        
         response.sendRedirect("");
         session.invalidate();
         return;
      }
      if (session.isNew()) {
         // first time we get sessionID and userName
         userSessionsInCreation.add(session);
         messageSystem.sendMessage(new MsgGetUser(address, messageSystem.getAddressService()
               .getAddress(AccountService.class), session, userName));
      }
      // if userSession creation is in process... then return wait page
      if (userSessionsInCreation.contains(session)) {
         response.getWriter().print(getWaitPage(session, userName));
      }
      else {
         // redirect to waiting for players page
         response.sendRedirect(frontendResource.getWaitingPath());
      }
   }

   public void deleteCreatedUserSession(HttpSession session) {
      if (session == null)
         throw new java.lang.IllegalArgumentException();
      userSessionsInCreation.remove(session);
   }
   
   public void unauthenticate(HttpSession unauthenticatedSession) {
      if (unauthenticatedSession == null)
         return;
      unauthenticated.add(unauthenticatedSession);
   }

   private void includeOkInfo(HttpServletResponse response) {
      response.setContentType("text/html;charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);
      // baseRequest.setHandled(true);
   }

   private String getWaitPage(HttpSession session, String userName) {
      Map<String, Object> pageVars = new HashMap<>();
      pageVars.put("sessionId", session.getId());
      pageVars.put("userName", userName);
      pageVars.put("refreshInterval", Integer.toString(ServerSettings.FRONTEND_TICK_IN_MILLIS));
      return PageGenerator.getPage(frontendResource.getTemplatesDir(), frontendResource.getLogonTemplate(), pageVars);
   }

}
