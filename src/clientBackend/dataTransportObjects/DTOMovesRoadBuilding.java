package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesRoadBuilding {
	String type = "Road_Building";
	int playerIndex;
	Map<String, String> spot1;
	Map<String, String> spot2;
	
	public DTOMovesRoadBuilding(int playerIndex, String spot1X, String spot1Y, String spot1Direction, String spot2X, String spot2Y, String spot2Direction) {
		this.playerIndex = playerIndex;
		
		spot1 = new HashMap<>();
		spot2 = new HashMap<>();
		
		spot1.put("x", spot1X);
		spot1.put("y", spot1Y);
		spot1.put("direction", spot1Direction);
		spot2.put("x", spot2X);
		spot2.put("y", spot2Y);
		spot2.put("direction", spot2Direction);
	}
}