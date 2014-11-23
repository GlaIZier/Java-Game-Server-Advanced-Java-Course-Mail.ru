package server.message_system.db_messages;

import server.db.DatabaseService;
import server.message_system.base.Abonent;
import server.message_system.base.Address;
import server.message_system.base.Msg;

public abstract class MsgToDatabaseService extends Msg{

   public MsgToDatabaseService(Address from, Address to) {
      super(from, to);
   }

   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof DatabaseService) 
         exec((DatabaseService) abonent);
      else 
         System.out.println("Wrong object in MsgToDatabaseService");
   }
   
   public abstract void exec(DatabaseService databaseService);

}
