package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesRobPlayer {
	String type = "robPlayer";
	int playerIndex;
	int victimIndex;
	Map<String, Object> location;

	public DTOMovesRobPlayer(int playerIndex, int victimIndex, String x, String y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;

		this.location = new HashMap<>();
		this.location.put("x", x);
		this.location.put("y", y);
	}
}
