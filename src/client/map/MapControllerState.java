package client.map;

import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public abstract class MapControllerState {
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		
		return false;
	}

	public boolean canPlaceSettlement(VertexLocation vertLov) {
		return false;
	}
}
