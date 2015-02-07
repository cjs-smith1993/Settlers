package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesRoadBuilding {
	String type = "Road_Building";
	PlayerNumber playerIndex;
	Map<String, Object> spot1;
	Map<String, Object> spot2;
	
	public DTOMovesRoadBuilding(PlayerNumber playerIndex, int spot1X, int spot1Y, EdgeDirection spot1Direction, int spot2X, int spot2Y, EdgeDirection spot2Direction) {
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