package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	String type = "buildRoad";
	int playerIndex;
	Boolean free = false;
	DTOEdgeLocation roadLocation;

	public DTOMovesBuildRoad(PlayerNumber playerIndex, int x, int y, EdgeDirection direction, Boolean free) {
		this.playerIndex = playerIndex.getInteger();
		this.free = free;
		this.roadLocation = new DTOEdgeLocation(x, y, direction);
	}
}
