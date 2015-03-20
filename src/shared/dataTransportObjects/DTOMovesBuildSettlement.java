package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildSettlement {
	public String type = "buildSettlement";
	public int playerIndex;
	public DTOVertexLocation vertexLocation;
	public Boolean free = false;

	public DTOMovesBuildSettlement(
			PlayerNumber playerIndex,
			int x,
			int y,
			VertexDirection direction,
			Boolean free) {
		this.playerIndex = playerIndex.getInteger();
		this.vertexLocation = new DTOVertexLocation(x, y, direction);
		this.free = free;
	}
}
