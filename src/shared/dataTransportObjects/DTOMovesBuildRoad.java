package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	public String type = "buildRoad";
	public int playerIndex;
	public Boolean free = false;
	public DTOEdgeLocation roadLocation;

	public DTOMovesBuildRoad(
			PlayerNumber playerIndex,
			int x,
			int y,
			EdgeDirection direction,
			Boolean free) {
		this.playerIndex = playerIndex.getInteger();
		this.free = free;
		this.roadLocation = new DTOEdgeLocation(x, y, direction);
	}
}
