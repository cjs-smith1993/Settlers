package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildCity {
	String type = "buildCity";
	int playerIndex;
	DTOVertexLocation vertexLocation;

	public DTOMovesBuildCity(PlayerNumber playerIndex, int x, int y, VertexDirection direction) {
		this.playerIndex = playerIndex.getInteger();

		this.vertexLocation = new DTOVertexLocation(x, y, direction);
	}
}
