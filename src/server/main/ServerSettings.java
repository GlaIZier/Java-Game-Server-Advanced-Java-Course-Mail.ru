package server.main;

public final class ServerSettings {

   public static final String START_WITHOUT_JOIN_ARG = "join_server=false";

   public static final int FRONTEND_TICK_IN_MILLIS = 1000;

   public static final int SERVER_TICK_IN_MILLIS = 100;

   public static final int DB_TEST_DELAY_IN_MILLIS = 1000;

   public static final int PORT = 8080;

   public static final String RESOURCE_FOLDER = "static";

   public static final String DATA_FOLDER = "data";
   
   private ServerSettings() {}

}
