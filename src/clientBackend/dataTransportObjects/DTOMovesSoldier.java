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
		
		location = new HashMap<>();
		location.put("x", x);
		location.put("y", y);
	}
}
