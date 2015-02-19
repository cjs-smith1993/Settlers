package clientBackend.dataTransportObjects;

import shared.locations.VertexDirection;

public class DTOMovesBuildCity {
	String type = "buildCity";
	int playerIndex;
	DTOVertexLocation vertexLocation;

	public DTOMovesBuildCity(int playerIndex, int x, int y, VertexDirection direction) {
		this.playerIndex = playerIndex;

		this.vertexLocation = new DTOVertexLocation(x, y, direction);
	}
}
