package server.game;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import server.frontend.Game;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.MessageSystem;
import server.message_system.frontend_messages.game.MsgAddPlayer;
import server.users.UserSession;
import server.utils.Context;

public class GameMechanics implements Runnable, Abonent {
   
   public static final int PLAYERS_NUMBER_IN_GAME = 2;
   
   public static final int GAME_TIME_IN_SECONDS = 2;
   
   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private Queue <UserSession> waitingPlayers = new PriorityQueue<>();
   
   private Map<UserSession, GameSession> players = new HashMap<>();
   
   public GameMechanics(Context context) {
      this.messageSystem = (MessageSystem) context.getImplementation(MessageSystem.class);
      this.address = new Address();
      this.messageSystem.addService(this);
   }

   public GameMechanics(MessageSystem messageSystem) {
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

   public void addWaitingPlayer(UserSession userSession) {
      if (userSession == null) 
         throw new IllegalArgumentException();
      if (waitingPlayers.contains(userSession)) {
         System.out.println("Waiting player with session " + userSession.getSession() +
               "is already in waitingPlayers set. Can't add one more time!");
         return;
      }
      waitingPlayers.add(userSession);
      createGameSessions();
   }

   private void createGameSessions() {
      while (waitingPlayers.size() >= PLAYERS_NUMBER_IN_GAME) {
         UserSession first = waitingPlayers.poll();
         UserSession second = waitingPlayers.poll();
         GameSession gameSession = new GameSession(first, second);
         players.put(first, gameSession);
         players.put(second, gameSession);
         messageSystem.sendMessage(new MsgAddPlayer(address, messageSystem.getAddressService()
               .getAddress(Game.class), first) );
         messageSystem.sendMessage(new MsgAddPlayer(address, messageSystem.getAddressService()
               .getAddress(Game.class), second) );
      }
//    Set<UserSession> gameSessionChunk = new HashSet<>();
//    for (UserSession us : waitingPlayers) {
//       gameSessionChunk.add(us);
//       // if added a full chunk of players in game
//       if (gameSessionChunk.size() % PLAYERS_NUMBER_IN_GAME == 0) {
//          GameSession newGameSession = new GameSession(gameSessionChunk);
//          // add new players to players
//          for (UserSession userInNewGameSession : gameSessionChunk) {
//             players.put(userInNewGameSession, newGameSession);
//             waitingPlayers.remove(userInNewGameSession);
//          }
//          gameSessionChunk.clear();
//       }
//    }
   }
}
