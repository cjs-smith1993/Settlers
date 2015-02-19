package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	String type = "buildRoad";
	int playerIndex;
	Boolean free = false;
	Map<String, Object> roadLocation;

	public DTOMovesBuildRoad(int playerIndex, int x, int y, EdgeDirection direction, Boolean free) {
		this.playerIndex = playerIndex;

		this.roadLocation = new HashMap<>();
		this.roadLocation.put("x", x);
		this.roadLocation.put("y", y);
		this.roadLocation.put("direction", direction);
		this.free = free;
	}
}
