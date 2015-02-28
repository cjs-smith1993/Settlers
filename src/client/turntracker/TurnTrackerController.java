package client.turntracker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.PlayerNumber;
import client.base.*;
import client.data.PlayerInfo;
import clientBackend.model.Facade;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	Facade facade = Facade.getInstance();

	public TurnTrackerController(ITurnTrackerView view) {
		super(view);
		this.facade.addObserver(this);
	}

	@Override
	public ITurnTrackerView getView() {
		return (ITurnTrackerView) super.getView();
	}

	@Override
	public void endTurn() {

	}

	@Override
	public void update(Observable o, Object arg) {
		ITurnTrackerView view = this.getView();
		List<PlayerInfo> players = this.facade.getPlayers();

		if (players != null) {
			for (PlayerInfo playerInfo : players) {
				if (playerInfo != null) {
					view.initializePlayer(playerInfo.getPlayerIndex().getInteger(),
							playerInfo.getName(), playerInfo.getColor());

					PlayerNumber clientIndex = this.facade.getClientPlayerIndex();
					if (playerInfo.getPlayerIndex() == clientIndex) {
						view.setLocalPlayerColor(playerInfo.getColor());
					}
				}
			}

			PlayerNumber longestRoadPlayer = facade.getLongestRoadPlayer();
			PlayerNumber largestArmyPlayer = facade.getLargestArmyPlayer();
			
			for (PlayerInfo p : players) {
				view.updatePlayer(p.getPlayerIndex().getInteger(), facade.getPlayerScore(p.getPlayerIndex()), facade.isClientTurn(), largestArmyPlayer == p.getPlayerIndex(), longestRoadPlayer == p.getPlayerIndex());
			}
		}
	}
}
