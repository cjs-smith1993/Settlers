package client.map.state;

import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.map.IMapView;
import client.map.IRobView;
import clientBackend.model.Facade;

public class SetupMapControllerState extends DefaultMapControllerState {

	public SetupMapControllerState(Facade facade, IMapView view, IRobView robView) {
		super(facade, view, robView);
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		System.out.println("can place road at " + edge + " for FREE");
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, true);
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		System.out.println("can place settlement at " + vertex + " for FREE");
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, true);
	}

}
