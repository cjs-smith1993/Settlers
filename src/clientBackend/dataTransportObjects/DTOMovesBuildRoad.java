package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	String type = "buildRoad";
	PlayerNumber playerIndex;
	Boolean free = false;
	Map<String, Object> roadLocation;
	
	public DTOMovesBuildRoad(PlayerNumber playerIndex, int x, int y, EdgeDirection direction, Boolean free) {
		this.playerIndex = playerIndex;
		
		roadLocation = new HashMap<>();
		roadLocation.put("x", x);
		roadLocation.put("y", y);
		roadLocation.put("direction", direction);
		this.free = free;
	}
}
