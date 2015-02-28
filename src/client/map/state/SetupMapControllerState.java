package client.map.state;

import shared.definitions.CatanState;
import shared.definitions.PieceType;
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

	public void initFromModel() {
		super.initFromModel();

		if (this.facade.isGameReady()) {
			CatanState state = this.facade.getModelState();
			PieceType type = PieceType.SETTLEMENT;
			this.view.startDrop(type, this.facade.getClientPlayerColor(), false);
		}

	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, true);
	}

	public void placeRoad(EdgeLocation edge) {
		if (this.canPlaceRoad(edge)) {
			try {
				this.facade.buildRoad(this.facade.getClientPlayerIndex(), edge, true, true);
				this.view.placeRoad(edge, this.facade.getClientPlayerColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, true);
	}

	public void placeSettlement(VertexLocation vertex) {
		if (this.canPlaceSettlement(vertex)) {
			try {
				this.facade.buildSettlement(this.facade.getClientPlayerIndex(), vertex, true,
						true);
				this.view.placeSettlement(vertex, this.facade.getClientPlayerColor());
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}
}
