package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildCity {
	String type = "buildCity";
	PlayerNumber playerIndex;
	DTOVertexLocation vertexLocation;

	public DTOMovesBuildCity(PlayerNumber playerIndex, int x, int y, VertexDirection direction) {
		this.playerIndex = playerIndex;

		this.vertexLocation = new DTOVertexLocation(x, y, direction);
	}
}
