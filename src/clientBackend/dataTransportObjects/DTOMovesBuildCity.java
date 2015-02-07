package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildCity {
	String type = "buildCity";
	int playerIndex;
	Map<String, Object> vertexLocation;
	
	public DTOMovesBuildCity(int playerIndex, int x, int y, VertexDirection direction) {
		this.playerIndex = playerIndex;
		
		vertexLocation = new HashMap<>();
		vertexLocation.put("x", x);
		vertexLocation.put("y", y);
		vertexLocation.put("direction", direction);
	}
}
