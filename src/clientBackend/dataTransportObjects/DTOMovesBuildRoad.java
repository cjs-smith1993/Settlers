package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	String type = "buildRoad";
	PlayerNumber playerIndex;
	Boolean free = false;
	DTOEdgeLocation roadLocation;

	public DTOMovesBuildRoad(PlayerNumber playerIndex, int x, int y, EdgeDirection direction, Boolean free) {
		this.playerIndex = playerIndex;

		this.roadLocation = new DTOEdgeLocation(x, y, direction);
	}
}
