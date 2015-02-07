package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;

public class DTOMovesRobPlayer {
	String type = "robPlayer";
	int playerIndex;
	int victimIndex;
	Map<String, Object> location;
	
	public DTOMovesRobPlayer(int playerIndex, int victimIndex, String x, String y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;
		
		location = new HashMap<>();
		location.put("x", x);
		location.put("y", y);
	}
}
