package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;

public class DTOMovesSoldier {
	String type = "Soldier";
	PlayerNumber playerIndex;
	PlayerNumber victimIndex;
	Map<String, Object> location;
	
	public DTOMovesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex, String x, String y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;
		
		location = new HashMap<>();
		location.put("x", x);
		location.put("y", y);
	}
}
