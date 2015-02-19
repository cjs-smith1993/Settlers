package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.locations.EdgeDirection;

public class DTOMovesRoadBuilding {
	String type = "Road_Building";
	int playerIndex;
	Map<String, Object> spot1;
	Map<String, Object> spot2;

	public DTOMovesRoadBuilding(int playerIndex, int spot1X, int spot1Y,
			EdgeDirection spot1Direction, int spot2X, int spot2Y, EdgeDirection spot2Direction) {
		this.playerIndex = playerIndex;

		this.spot1 = new HashMap<>();
		this.spot2 = new HashMap<>();

		this.spot1.put("x", spot1X);
		this.spot1.put("y", spot1Y);
		this.spot1.put("direction", spot1Direction);
		this.spot2.put("x", spot2X);
		this.spot2.put("y", spot2Y);
		this.spot2.put("direction", spot2Direction);
	}
}