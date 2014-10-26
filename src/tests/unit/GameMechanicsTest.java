package tests.unit;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import server.frontend.Game;
import server.game.GameMechanics;
import server.message_system.base.MessageSystem;
import server.users.User;
import server.users.UserSession;

public class GameMechanicsTest {

   private GameMechanics gm;

   private MessageSystem ms;

   @Before
   public void setUp() throws Exception {
      ms = MessageSystem.getInstance();
      gm = new GameMechanics(ms);
   }

   @Test
   public void testAddWaitingPlayer() {
      UserSession userSession = new UserSession(null, new User("test", 1) );
      gm.addWaitingPlayer(userSession);   
      try {
         Field wp = gm.getClass().getDeclaredField("waitingPlayers");
         wp.setAccessible(true);
         Queue<UserSession> wpq =  (Queue<UserSession>) wp.get(gm);
         Method getWaitingPlayer = Class.forName("java.util.Queue").getDeclaredMethod("peek");  
         assertEquals((UserSession) getWaitingPlayer.invoke(wpq), userSession);
         // Could do easier, but wanted to try Method reflection (wpq.peek()): 
         // assertEquals(wpq.peek(), userSession);
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   // add Mockito to test this method. Too hard to test
   @Test 
   public void testAddPlayerClicks() {
      new Game(ms); // need new Game because we send msgs while creating game sessions and finishing games;
      
      HttpSession session1 = Mockito.mock(HttpSession.class);
      Mockito.when(session1.getId()).thenReturn("id1");
      HttpSession session2 = Mockito.mock(HttpSession.class);
      Mockito.when(session2.getId()).thenReturn("id2");
      
      UserSession userSession1 = new UserSession(session1, new User("test1", 1) );
      UserSession userSession2 = new UserSession(session2, new User("test2", 2) );
      
      gm.addWaitingPlayer(userSession1);
      gm.addWaitingPlayer(userSession2);
      gm.addPlayersClicks(userSession1, 10);
      
      assertEquals(userSession1.getClickedByMe(), 10);
   }
   
}
