package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesBuildSettlement {
	String type = "buildSettlement";
	int playerIndex;
	Map<String, Object> vertexLocation;
	Boolean free = false;
	
	public DTOMovesBuildSettlement(int playerIndex, int x, int y, String direction, Boolean free) {
		this.playerIndex = playerIndex;
		
		vertexLocation = new HashMap<>();
		vertexLocation.put("x", x);
		vertexLocation.put("y", y);
		vertexLocation.put("direction", direction);
		
		this.free = free;
	}
}
