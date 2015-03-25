package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;

public class DTOMovesRoadBuilding {
	public String type = "Road_Building";
	public int playerIndex;
	public DTOEdgeLocation spot1;
	public DTOEdgeLocation spot2;

	public DTOMovesRoadBuilding(
			PlayerNumber playerIndex,
			int x1,
			int y1,
			EdgeDirection dir1,
			int x2,
			int y2,
			EdgeDirection dir2) {

		this.playerIndex = playerIndex.getInteger();
		this.spot1 = new DTOEdgeLocation(x1, y1, dir1);
		this.spot2 = new DTOEdgeLocation(x2, y2, dir2);
	}
}