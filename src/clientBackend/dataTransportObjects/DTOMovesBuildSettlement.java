package clientBackend.dataTransportObjects;

import shared.locations.VertexDirection;

public class DTOMovesBuildSettlement {
	String type = "buildSettlement";
	int playerIndex;
	DTOVertexLocation vertexLocation;
	Boolean free = false;

	public DTOMovesBuildSettlement(int playerIndex, int x, int y, VertexDirection direction,
			Boolean free) {
		this.playerIndex = playerIndex;

		this.vertexLocation = new DTOVertexLocation(x, y, direction);

		this.free = free;
	}
}
