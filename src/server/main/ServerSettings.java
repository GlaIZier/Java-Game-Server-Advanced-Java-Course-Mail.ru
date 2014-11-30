package server.main;

public final class ServerSettings {

	public static final String START_WITHOUT_JOIN_ARG = "join_server=false";

	public static final int PORT = 8080;

	public static final int FRONTEND_TICK_IN_MILLIS = 1000;

	public static final int SERVER_TICK_IN_MILLIS = 100;

	public static final int DB_TEST_DELAY_IN_MILLIS = 1000;

	public static final String RESOURCE_FOLDER = "static";

	public static final String DATA_FOLDER = "data";

	public static final String DB_SERVER_URL = "jdbc:mysql://localhost:3306/";
	
	public static final String DB_NAME = "game_server_db";
	
	public static final String DB_LOGIN = "GlaIZier";
	
	public static final String DB_PASSWORD = "password";
	
	public static final String DB_USERS_TABLE_NAME = "users";

	private ServerSettings() {}

}
