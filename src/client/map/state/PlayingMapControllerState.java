package client.map.state;

import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.map.IMapView;
import client.map.IRobView;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

public class PlayingMapControllerState extends DefaultMapControllerState {

	public PlayingMapControllerState(Facade facade, IMapView view, IRobView robView) {
		super(facade, view, robView);
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		System.out.println("can place road at " + edge);
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, false);
	}

	public void placeRoad(EdgeLocation edge) {
		System.out.println("place road at " + edge);
		if (this.canPlaceRoad(edge)) {
			try {
				this.facade.buildRoad(this.facade.getClientPlayerIndex(), edge, false, false);
				this.view.placeRoad(edge, this.facade.getClientPlayer().getColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		System.out.println("can place settlement at " + vertex);
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, false);
	}

	public void placeSettlement(VertexLocation vertex) {
		System.out.println("place settlement at " + vertex);
		if (this.canPlaceSettlement(vertex)) {
			try {
				this.facade.buildSettlement(this.facade.getClientPlayerIndex(), vertex, false,
						false);
				this.view.placeSettlement(vertex, this.facade.getClientPlayer().getColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceCity(VertexLocation vertex) {
		System.out.println("can place city at " + vertex);
		return this.facade.canPlaceCity(this.facade.getClientPlayerIndex(), vertex);
	}

	public void placeCity(VertexLocation vertex) {
		System.out.println("place city at " + vertex);
		if (this.canPlaceCity(vertex)) {
			try {
				this.facade.buildCity(this.facade.getClientPlayerIndex(), vertex);
				this.view.placeCity(vertex, this.facade.getClientPlayer().getColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

}
