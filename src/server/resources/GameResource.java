package server.resources;

public class GameResource {
	
	private static int UNINITIALIZED = -1;
	
	private int playersNumber = UNINITIALIZED; 
	
	private int gameTimeInSec = UNINITIALIZED;

	public int getPlayersNumber() {
		return playersNumber;
	}

	public int getGameTimeInSec() {
		return gameTimeInSec;
	}

}
