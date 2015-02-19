package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.locations.VertexDirection;

public class DTOMovesBuildCity {
	String type = "buildCity";
	int playerIndex;
	Map<String, Object> vertexLocation;

	public DTOMovesBuildCity(int playerIndex, int x, int y, VertexDirection direction) {
		this.playerIndex = playerIndex;

		this.vertexLocation = new HashMap<>();
		this.vertexLocation.put("x", x);
		this.vertexLocation.put("y", y);
		this.vertexLocation.put("direction", direction);
	}
}
