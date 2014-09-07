package tests.unit;

import static org.junit.Assert.*;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import server.game.GameMechanics;
import server.game.GameSession;
import server.message_system.base.MessageSystem;
import server.users.AccountService;
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
      assertEquals(gm.getWaitingPlayer(userSession), userSession);
   }

   @Test 
   public void testAddPlayerClicks() {
      UserSession userSession = new UserSession(null, new User("test", 1) );
      gm.setPlayersClicks(userSession, 10);
      assertEquals(gm.getPlayersClicks(userSession), 10);
   }
   
   @Test
   public void testAddPlayer() {
      UserSession userSession1 = new UserSession(null, new User("test1", 1) );
      UserSession userSession2 = new UserSession(null, new User("test2", 2) );
      GameSession gs = new GameSession(userSession1, userSession2);
      gm.addPlayer(userSession1, userSession2, gs);
      assertEquals(gm.getPlayerGameSession(userSession1), gs);
      assertEquals(gm.getPlayerGameSession(userSession2), gs);
   }

//   @Test
//   public void testAddClicks() {
//      UserSession userSession1 = new UserSession(null, new User("test1", 1) );
//      UserSession userSession2 = new UserSession(null, new User("test2", 2) );
//      GameSession gs = new GameSession(userSession1, userSession2);
//      gm.addPlayer(userSession1, userSession2, gs);
//      gs =  gm.getPlayerGameSession(userSession1);
//      gs.addClicks(userSession1, 100);
//      assertEquals(gm.getPlayerGameSession(userSession1).getClicks(userSession1), 
//            gs.getClicks(userSession1) );
//   }

}
