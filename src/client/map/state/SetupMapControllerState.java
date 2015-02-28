package client.map.state;

import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.map.IMapView;
import client.map.IRobView;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

public class SetupMapControllerState extends DefaultMapControllerState {

	public SetupMapControllerState(Facade facade, IMapView view, IRobView robView) {
		super(facade, view, robView);
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		System.out.println("can place road at " + edge + " for FREE");
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, true);
	}

	public void placeRoad(EdgeLocation edge) {
		System.out.println("place road at " + edge);
		if (this.canPlaceRoad(edge)) {
			try {
				this.facade.buildRoad(this.facade.getClientPlayerIndex(), edge, true, true);
				this.view.placeRoad(edge, this.facade.getClientPlayer().getColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		System.out.println("can place settlement at " + vertex + " for FREE");
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, true);
	}

	public void placeSettlement(VertexLocation vertex) {
		System.out.println("place settlement at " + vertex);
		if (this.canPlaceSettlement(vertex)) {
			try {
				this.facade.buildSettlement(this.facade.getClientPlayerIndex(), vertex, true,
						true);
				this.view.placeSettlement(vertex, this.facade.getClientPlayer().getColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}
}
