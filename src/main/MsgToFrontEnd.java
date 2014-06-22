package main;

public abstract class MsgToFrontEnd extends Msg {

   public MsgToFrontEnd(Address from, Address to) {
      super(from, to);
   }

   @Override
   public void exec(Abonent abonent) {
      if (abonent instanceof FrontEnd) 
         exec((FrontEnd) abonent);
      else 
         System.out.println("Wrong object in MsgToFrontEnd");
   }
   
   public abstract void exec(FrontEnd frontEnd);
}
