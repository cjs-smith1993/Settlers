package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesSoldier {
	String type = "Soldier";
	int playerIndex;
	int victimIndex;
	Map<String, Object> location;

	public DTOMovesSoldier(int playerIndex, int victimIndex, String x, String y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;

		this.location = new HashMap<>();
		this.location.put("x", x);
		this.location.put("y", y);
	}
}
