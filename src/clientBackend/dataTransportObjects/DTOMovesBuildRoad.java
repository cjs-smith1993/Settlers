package clientBackend.dataTransportObjects;

import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	String type = "buildRoad";
	int playerIndex;
	Boolean free = false;
	DTOEdgeLocation roadLocation;

	public DTOMovesBuildRoad(int playerIndex, int x, int y, EdgeDirection direction, Boolean free) {
		this.playerIndex = playerIndex;

		this.roadLocation = new DTOEdgeLocation(x, y, direction);
	}
}
