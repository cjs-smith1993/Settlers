package client.map.state;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.PieceType;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.map.IMapView;
import client.map.IRobView;
import client.map.MapController;
import clientBackend.model.CatanException;
import clientBackend.model.Dwelling;
import clientBackend.model.Facade;

public class RobbingMapControllerState extends DefaultMapControllerState {

	public RobbingMapControllerState(
			Facade facade,
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
		return this.facade.canPlaceRobber(this.facade.getClientPlayerIndex(), hex);
	}

	public void placeRobber(HexLocation hex) {
		if (this.canPlaceRobber(hex)) {
			Collection<PlayerInfo> players = this.facade.getPlayers();
			Collection<Dwelling> dwellings = this.facade.getBoard().getAdjacentDwellings(hex);
			Collection<RobPlayerInfo> robbablePlayers = new ArrayList<RobPlayerInfo>();

			for (Dwelling dwelling : dwellings) {
				PlayerNumber ownerIdx = dwelling.getOwner();

				for (PlayerInfo info : players) {
					if (ownerIdx == info.getPlayerIndex()) {
						RobPlayerInfo robInfo = new RobPlayerInfo();

						robInfo.setId(info.getId());
						robInfo.setPlayerIndex(info.getPlayerIndex());
						robInfo.setName(info.getName());
						robInfo.setColor(info.getColor());
						int numCards = this.facade.getResourceCount(ownerIdx, ResourceType.ALL);
						robInfo.setNumCards(numCards);

						if (numCards > 0 && !robbablePlayers.contains(robInfo)) {
							robbablePlayers.add(robInfo);
						}
					}
				}
			}

			this.view.placeRobber(hex);
			this.controller.setRobberLocation(hex);

			RobPlayerInfo[] candidateVictims = robbablePlayers.toArray(new RobPlayerInfo[0]);
			this.robView.setPlayers(candidateVictims);
			this.robView.showModal();
		}
	}

	public void robPlayer(RobPlayerInfo victim) {
		try {
			PlayerNumber clientIndex = this.facade.getClientPlayerIndex();
			PlayerNumber victimIndex = victim.getPlayerIndex();
			HexLocation hex = this.controller.getRobberLocation();
			this.facade.robPlayer(clientIndex, victimIndex, hex);
			this.facade.finishTurn(this.facade.getClientPlayerIndex());
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
