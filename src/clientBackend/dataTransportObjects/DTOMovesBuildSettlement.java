package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.locations.VertexDirection;

public class DTOMovesBuildSettlement {
	String type = "buildSettlement";
	int playerIndex;
	Map<String, Object> vertexLocation;
	Boolean free = false;

	public DTOMovesBuildSettlement(int playerIndex, int x, int y, VertexDirection direction,
			Boolean free) {
		this.playerIndex = playerIndex;

		this.vertexLocation = new HashMap<>();
		this.vertexLocation.put("x", x);
		this.vertexLocation.put("y", y);
		this.vertexLocation.put("direction", direction);

		this.free = free;
	}
}
