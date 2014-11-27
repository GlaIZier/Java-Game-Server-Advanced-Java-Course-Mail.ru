package tests.unit;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import server.frontend.Game;
import server.game.GameMechanics;
import server.main.ServerSettings;
import server.message_system.base.MessageSystem;
import server.users.User;
import server.users.UserSession;
import server.utils.Context;
import server.utils.Vfs;
import server.utils.XmlFileSaxReader;

public class GameMechanicsTest {

   private GameMechanics gm;

   private Context context;

   @Before
   public void setUp() throws Exception {
      // prev before resources have been added
      //context = new Context(new AbstractMap.SimpleEntry<Class<?>, Object>(MessageSystem.class, MessageSystem.getInstance()));
      
      Map<Class<?>, Object> servicesToImpl = new HashMap<>();
      // put MsgSystem
      servicesToImpl.put(MessageSystem.class, MessageSystem.getInstance());
      
      // put all resources
      Vfs vfs = new Vfs(ServerSettings.DATA_FOLDER);
      Iterator<String> resourceIterator = vfs.bfsWoStartDir("");
      while (resourceIterator.hasNext()) {
         XmlFileSaxReader fileReader = new XmlFileSaxReader(resourceIterator.next());
         servicesToImpl.put(fileReader.getInstanceClass(), fileReader.getInstance());
      }
      
      context = new Context(servicesToImpl);
      gm = new GameMechanics(context);
   }

   @Test
   @SuppressWarnings("unchecked")
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
      new Game(context); // need new Game because we send msgs while creating game sessions and finishing games;
      
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
