package clientBackend.dataTransportObjects;

import java.util.Map;

public class DTOMovesRobPlayer {
	String type = "robPlayer";
	int playerIndex;
	int victimIndex;
	Map<String, String> location;
	
	public DTOMovesRobPlayer(int playerIndex, int victimIndex, String x, String y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;
		location.put("x", x);
		location.put("y", y);
	}
}
