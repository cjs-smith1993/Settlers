package client.map.state;

import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.map.IMapView;
import client.map.IRobView;
import client.map.MapController;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

public class PlayingMapControllerState extends DefaultMapControllerState {

	public PlayingMapControllerState(
			Facade facade,
			MapController controller,
			IMapView view,
			IRobView robView) {
		super(facade, controller, view, robView);
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, false);
	}

	public void placeRoad(EdgeLocation edge) {
		if (this.canPlaceRoad(edge)) {
			try {
				this.facade.buildRoad(this.facade.getClientPlayerIndex(), edge, false, false);
				this.view.placeRoad(edge, this.facade.getClientPlayerColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, false);
	}

	public void placeSettlement(VertexLocation vertex) {
		if (this.canPlaceSettlement(vertex)) {
			try {
				this.facade.buildSettlement(this.facade.getClientPlayerIndex(), vertex, false,
						false);
				this.view.placeSettlement(vertex, this.facade.getClientPlayerColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceCity(VertexLocation vertex) {
		return this.facade.canPlaceCity(this.facade.getClientPlayerIndex(), vertex);
	}

	public void placeCity(VertexLocation vertex) {
		if (this.canPlaceCity(vertex)) {
			try {
				this.facade.buildCity(this.facade.getClientPlayerIndex(), vertex);
				this.view.placeCity(vertex, this.facade.getClientPlayerColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		this.controller.setModalShowing(true);
		this.view.startDrop(pieceType, this.facade.getClientPlayerColor(), true);
	}

	public void cancelMove() {
		System.out.println("cancel move");
	}

	public void playRoadBuildingCard() {
		System.out.println("play road building");
	}

}
