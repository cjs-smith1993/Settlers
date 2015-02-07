package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;
import shared.locations.VertexDirection;

public class DTOMovesBuildSettlement {
	String type = "buildSettlement";
	PlayerNumber playerIndex;
	Map<String, Object> vertexLocation;
	Boolean free = false;
	
	public DTOMovesBuildSettlement(PlayerNumber playerIndex, int x, int y, VertexDirection direction, Boolean free) {
		this.playerIndex = playerIndex;
		
		vertexLocation = new HashMap<>();
		vertexLocation.put("x", x);
		vertexLocation.put("y", y);
		vertexLocation.put("direction", direction);
		
		this.free = free;
	}
}
