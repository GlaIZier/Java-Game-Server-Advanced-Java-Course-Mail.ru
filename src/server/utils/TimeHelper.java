package server.utils;

public class TimeHelper {
   
   public static final int FRONTEND_TICK_IN_MILLIS = 1000;
   
   public static final int SERVER_TICK_IN_MILLIS = 100;
   
   public static final int DB_TEST_DELAY_IN_MILLIS = 5000;
   
   public static void sleep(int millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
