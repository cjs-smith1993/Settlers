package client.map.state;

import shared.definitions.CatanState;
import shared.definitions.PieceType;
import shared.definitions.PropertyType;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.map.IMapView;
import client.map.IRobView;
import client.map.MapController;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

public class SetupMapControllerState extends DefaultMapControllerState {

	public SetupMapControllerState(
			Facade facade,
			MapController controller,
			IMapView view,
			IRobView robView) {
		super(facade, controller, view, robView);
	}

	public void initFromModel() {
		super.initFromModel();

		if (!this.controller.isModalShowing() && this.facade.isGameReady()) {
			int maxSettlements = 5;
			int remainingSettlements = this.facade.getHoldingCount(PropertyType.SETTLEMENT);
			int numSettlements = maxSettlements - remainingSettlements;

			CatanState state = this.facade.getModelState();
			int expectedSettlements = state == CatanState.FIRST_ROUND ? 0 : 1;
			PieceType type = numSettlements == expectedSettlements ? PieceType.SETTLEMENT
					: PieceType.ROAD;

			this.startMove(type, true, true);
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
				this.controller.setModalShowing(false);
				this.facade.finishTurn(this.facade.getClientPlayerIndex());
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
				this.view.startDrop(PieceType.ROAD, this.facade.getClientPlayerColor(), false);
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		this.controller.setModalShowing(true);
		this.view.startDrop(pieceType, this.facade.getClientPlayerColor(), false);
	}
}
