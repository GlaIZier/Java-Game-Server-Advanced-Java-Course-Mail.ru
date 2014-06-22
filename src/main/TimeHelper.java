package main;

public class TimeHelper {
   
   private static final int FRONTEND_TICK = 1000;
   
   private static final int SERVER_TICK = 100;
   
   private static final int DB_TEST_DELAY = 5000;

   public static int getFrontEndTick() {
      return FRONTEND_TICK;
   }

   public static int getServerTick() {
      return SERVER_TICK;
   }
   
   public static int getDBTestDelay() {
      return DB_TEST_DELAY;
   }
   
   public static void sleep(int millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
