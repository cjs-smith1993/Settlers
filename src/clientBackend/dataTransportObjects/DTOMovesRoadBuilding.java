package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesRoadBuilding {
	String type = "Road_Building";
	int playerIndex;
	DTOEdgeLocation spot1;
	DTOEdgeLocation spot2;

	public DTOMovesRoadBuilding(PlayerNumber playerIndex, int x1, int y1,
			EdgeDirection dir1, int x2, int y2, EdgeDirection dir2) {
		this.playerIndex = playerIndex.getInteger();

		this.spot1 = new DTOEdgeLocation(x1, y1, dir1);
		this.spot2 = new DTOEdgeLocation(x2, y2, dir2);
	}
}