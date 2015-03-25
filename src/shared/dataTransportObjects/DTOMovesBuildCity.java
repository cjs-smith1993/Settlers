package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildCity {
	public String type = "buildCity";
	public int playerIndex;
	public DTOVertexLocation vertexLocation;

	public DTOMovesBuildCity(PlayerNumber playerIndex, int x, int y, VertexDirection direction) {
		this.playerIndex = playerIndex.getInteger();
		this.vertexLocation = new DTOVertexLocation(x, y, direction);
	}
}
