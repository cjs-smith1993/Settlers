package client.turntracker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import client.base.*;
import client.data.PlayerInfo;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	Facade facade = Facade.getInstance();

	private final String ROLL_MESSAGE = "Roll the Dice";
	private final String ROBBER_MESSAGE = "Place the Robber";
	private final String FINISH_MESSAGE = "Finish Turn";
	private final String WAITING_MESSAGE = "Waiting for Other Players";

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
		try {
			this.facade.finishTurn(this.facade.getClientPlayerIndex());
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	public void updateView() {
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

			PlayerNumber longestRoadPlayer = this.facade.getLongestRoadPlayer();
			PlayerNumber largestArmyPlayer = this.facade.getLargestArmyPlayer();

			for (PlayerInfo p : players) {
				view.updatePlayer(p.getPlayerIndex().getInteger(),
						this.facade.getPlayerScore(p.getPlayerIndex()), this.facade.isClientTurn(),
						largestArmyPlayer == p.getPlayerIndex(),
						longestRoadPlayer == p.getPlayerIndex());
			}
		}
	}

	public void setButton() {
		if (!this.facade.isClientTurn()) {
			this.getView().updateGameState(this.WAITING_MESSAGE, false);
		}
		else {
			CatanState state = this.facade.getModelState();
			String message = this.FINISH_MESSAGE;

			if (state == CatanState.ROLLING) {
				message = this.ROLL_MESSAGE;
			}
			else if (state == CatanState.ROBBING) {
				message = this.ROBBER_MESSAGE;
			}

			this.getView().updateGameState(message, true);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (!this.facade.isGameReady()) {
			return;
		}

		this.updateView();
		this.setButton();
	}
}
