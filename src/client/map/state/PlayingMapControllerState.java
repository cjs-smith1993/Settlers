package client.map.state;

import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.map.IMapView;
import client.map.IRobView;
import clientBackend.model.Facade;

public class PlayingMapControllerState extends DefaultMapControllerState {

	public PlayingMapControllerState(Facade facade, IMapView view, IRobView robView) {
		super(facade, view, robView);
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		System.out.println("can place road at " + edge);
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, false);
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		System.out.println("can place settlement at " + vertex);
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, false);
	}

	public boolean canPlaceCity(VertexLocation vertex) {
		System.out.println("can place city at " + vertex);
		return this.facade.canPlaceCity(this.facade.getClientPlayerIndex(), vertex);
	}

}
