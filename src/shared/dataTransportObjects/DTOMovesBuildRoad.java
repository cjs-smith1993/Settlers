package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

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

		if (playerIndex == null || direction == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
		this.free = free;
		this.roadLocation = new DTOEdgeLocation(x, y, direction);
	}
}
