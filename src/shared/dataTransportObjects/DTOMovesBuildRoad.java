package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesBuildRoad {
	public String type = "buildRoad";
	public PlayerNumber playerIndex;
	public Boolean free = false;
	public DTOEdgeLocation roadLocation;

	public DTOMovesBuildRoad(
			PlayerNumber playerIndex,
			int x,
			int y,
			EdgeDirection direction,
			Boolean free) {

		this.playerIndex = playerIndex;
		this.free = free;
		this.roadLocation = new DTOEdgeLocation(x, y, direction);
	}
}
