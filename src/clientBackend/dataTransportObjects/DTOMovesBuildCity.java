package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesBuildCity {
	String type = "buildCity";
	int playerIndex;
	Map<String, String> vertexLocation;
	
	public DTOMovesBuildCity(int playerIndex, String x, String y, String direction) {
		this.playerIndex = playerIndex;
		
		vertexLocation = new HashMap<>();
		vertexLocation.put("x", x);
		vertexLocation.put("y", y);
		vertexLocation.put("direction", direction);
	}
}
