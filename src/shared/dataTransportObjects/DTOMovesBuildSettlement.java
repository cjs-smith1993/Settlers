package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildSettlement {
	String type = "buildSettlement";
	int playerIndex;
	DTOVertexLocation vertexLocation;
	Boolean free = false;

	public DTOMovesBuildSettlement(PlayerNumber playerIndex, int x, int y, VertexDirection direction,
			Boolean free) {
		this.playerIndex = playerIndex.getInteger();

		this.vertexLocation = new DTOVertexLocation(x, y, direction);

		this.free = free;
	}
}
