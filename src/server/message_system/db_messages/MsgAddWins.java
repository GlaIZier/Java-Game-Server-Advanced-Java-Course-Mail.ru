package server.message_system.db_messages;

import server.db.DatabaseService;
import server.message_system.base.Address;

public class MsgAddWins extends MsgToDatabaseService {
   
   private final String userName;
   
   private final int wins;

   public MsgAddWins(Address from, Address to, String userName, int wins) {
      super(from, to);
//      if (winsNum <= 0) {
//         System.out.println("Trying to add incorrect number of wins in message to DB service: " + winsNum + " for user " + userName);
//         return;
//      }
      this.userName = userName;
      this.wins = wins;
   }

   @Override
   public void exec(DatabaseService databaseService) {
      databaseService.addWins(userName, wins);
   }

}
