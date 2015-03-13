package client.frontend.map.state;

import client.backend.ClientFacade;
import client.frontend.data.RobPlayerInfo;
import client.frontend.map.IMapView;
import client.frontend.map.IRobView;
import client.frontend.map.MapController;
import shared.definitions.CatanState;
import shared.definitions.PieceType;
import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;
import shared.model.CatanException;

public class RobbingMapControllerState extends DefaultMapControllerState {

	public RobbingMapControllerState(
			ClientFacade facade,
			MapController controller,
			IMapView view,
			IRobView robView) {
		super(facade, controller, view, robView);
	}

	public void initFromModel() {
		super.initFromModel();
		if (!this.controller.isModalShowing() && this.facade.isGameReady()
				&& this.facade.isClientTurn()) {
			this.startMove(PieceType.ROBBER, false, false);
		}
	}

	public boolean canPlaceRobber(HexLocation hex) {
		return this.facade.canPlaceRobber(this.facade.getClientPlayerIndex(), hex,
				CatanState.ROBBING);
	}

	public void robPlayer(RobPlayerInfo victim) {
		try {
			PlayerNumber clientIndex = this.facade.getClientPlayerIndex();
			PlayerNumber victimIndex = victim.getPlayerIndex();
			HexLocation hex = this.controller.getRobberLocation();
			this.facade.robPlayer(clientIndex, victimIndex, hex, CatanState.ROBBING);
			this.controller.setModalShowing(false);
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		this.controller.setModalShowing(true);
		this.view.startDrop(pieceType, this.facade.getClientPlayerColor(), false);
	}
}
