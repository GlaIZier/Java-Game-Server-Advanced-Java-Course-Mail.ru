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
import server.message_system.frontend_messages.game.MsgSendUpdatedPlayer;
import server.users.UserSession;
import server.utils.Context;

public class GameMechanics implements Runnable, Abonent {
   
   public static final int PLAYERS_NUMBER_IN_GAME = 2;
   
   public static final int GAME_TIME_IN_SECONDS = 2;
   
   private final MessageSystem messageSystem;
   
   private final Address address;
   
   private Queue <UserSession> waitingPlayers = new PriorityQueue<>();
   
   private Map<UserSession, GameSession> playersToGameSession = new HashMap<>();
   
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
         exchangeEnemies(first, second);
         first.setTimeToFinish(GAME_TIME_IN_SECONDS);
         second.setTimeToFinish(GAME_TIME_IN_SECONDS);
         GameSession gameSession = new GameSession(first, second);
         playersToGameSession.put(first, gameSession);
         playersToGameSession.put(second, gameSession);
         messageSystem.sendMessage(new MsgAddPlayer(address, messageSystem.getAddressService()
               .getAddress(Game.class), first) );
         messageSystem.sendMessage(new MsgAddPlayer(address, messageSystem.getAddressService()
               .getAddress(Game.class), second) );
      }
      /* Legacy Code
       Set<UserSession> gameSessionChunk = new HashSet<>();
       for (UserSession us : waitingPlayers) {
          gameSessionChunk.add(us);
          // if added a full chunk of players in game
          if (gameSessionChunk.size() % PLAYERS_NUMBER_IN_GAME == 0) {
             GameSession newGameSession = new GameSession(gameSessionChunk);
             // add new players to players
             for (UserSession userInNewGameSession : gameSessionChunk) {
                players.put(userInNewGameSession, newGameSession);
                waitingPlayers.remove(userInNewGameSession);
             }
             gameSessionChunk.clear();
          }
       }
       */
   }
   
   private void exchangeEnemies(UserSession player1, UserSession player2) {
      player1.setEnemy(player2.getMe() );
      player2.setEnemy(player1.getMe() );
   }
   
   public void addPlayersClicks(UserSession player, int clicks) {
      if (player == null || clicks < 0) 
         throw new IllegalArgumentException();
      if (!playersToGameSession.containsKey(player) )
         throw new IllegalArgumentException();
      player.addClicksToMe(clicks);
      player.finishClicks();
      GameSession gameSession = playersToGameSession.get(player);
      UserSession enemy = gameSession.getEnemyOf(player);
      System.out.println("Added clicks " + clicks + " to: " + player.getSession().getId() );
      if (enemy.finishedClicks() ) {
         finishGameSession(player, enemy, gameSession);
         //TODO Delete gameSession and players?
      }
   }
   
   private void finishGameSession(UserSession player, UserSession enemy, GameSession gameSession) {
      exchangeEnemiesClicks(player, enemy);
      checkAndWriteGameResults(player, enemy);
      gameSession.finishGame();
      messageSystem.sendMessage(new MsgSendUpdatedPlayer(address, 
            messageSystem.getAddressService().getAddress(Game.class), player) );
      messageSystem.sendMessage(new MsgSendUpdatedPlayer(address, 
            messageSystem.getAddressService().getAddress(Game.class), enemy) );
      deleteGameSessionForPlayers(player, enemy);
      System.out.println("Finish game in game session");
   }
   
   private void exchangeEnemiesClicks(UserSession player1, UserSession player2) {
      player1.setClickedByEnemy(player2.getClickedByMe() );
      player2.setClickedByEnemy(player1.getClickedByMe() );
   }
   
   private void checkAndWriteGameResults(UserSession player1, UserSession player2) {
      if (player1.getClickedByMe() > player2.getClickedByMe() ) {
         player1.setGameResult(GameResult.WIN);
         player2.setGameResult(GameResult.LOSS);
      }
      else if (player1.getClickedByMe() < player2.getClickedByMe() ) {
         player2.setGameResult(GameResult.WIN);
         player1.setGameResult(GameResult.LOSS);
      }
      else {
         player1.setGameResult(GameResult.DRAW);
         player2.setGameResult(GameResult.DRAW);
      }
   }
   
   private void deleteGameSessionForPlayers(UserSession player1, UserSession player2) {
      if (player1 == null || player2 == null) 
         throw new IllegalArgumentException();
      if (playersToGameSession.remove(player1) == null) 
         System.out.println("No such player: " + player1.getSession().getId() + "in GameMechanics. Couldn't delete it");
      if (playersToGameSession.remove(player2) == null) 
         System.out.println("No such player: " + player2.getSession().getId() + "in GameMechanics. Couldn't delete it");
   }
   
}
