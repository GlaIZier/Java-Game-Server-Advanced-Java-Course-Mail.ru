package server.game;

public enum GameResult {
   
   WIN("Congratulations! You win!"),
   
   LOSS("Sorry, you lost!"),
   
   DRAW("Draw!");
   
   private final String msg;
   
   private GameResult(final String msg) {
      this.msg = msg;
   }
   
   @Override
   public String toString() {
      return msg;
   }
   
}
