package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesBuildSettlement {
	String type = "buildSettlement";
	int playerIndex;
	Map<String, String> vertexLocation;
	Boolean free = false;
	
	public DTOMovesBuildSettlement(int playerIndex, String x, String y, String direction, Boolean free) {
		this.playerIndex = playerIndex;
		
		vertexLocation = new HashMap<>();
		vertexLocation.put("x", x);
		vertexLocation.put("y", y);
		vertexLocation.put("direction", direction);
		this.free = free;
	}
}
