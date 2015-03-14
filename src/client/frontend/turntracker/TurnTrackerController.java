package client.frontend.turntracker;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import client.backend.ClientModelFacade;
import client.frontend.base.*;
import client.frontend.data.PlayerInfo;
import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;

/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	ClientModelFacade facade = ClientModelFacade.getInstance();

	private boolean isStartup = true;

	private final String ROLL_MESSAGE = "Roll the Dice";
	private final String DISCARD_MESSAGE = "Wait for Other Players to Discard";
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
			if (this.isStartup) {
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

				this.isStartup = false;
			}

			PlayerNumber longestRoadPlayer = this.facade.getLongestRoadPlayer();
			PlayerNumber largestArmyPlayer = this.facade.getLargestArmyPlayer();

			for (PlayerInfo p : players) {
				PlayerNumber playerIndex = p.getPlayerIndex();
				int index = playerIndex.getInteger();
				int points = this.facade.getPlayerScore(playerIndex);
				boolean highlight = playerIndex == this.facade.getGame().getCurrentPlayer();
				boolean hasLargestArmy = playerIndex == largestArmyPlayer;
				boolean hasLongestRoad = playerIndex == longestRoadPlayer;
				view.updatePlayer(index, points, highlight, hasLargestArmy, hasLongestRoad);
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
			boolean enableButton = true;

			if (state == CatanState.ROLLING) {
				message = this.ROLL_MESSAGE;
				enableButton = false;
			}
			else if (state == CatanState.DISCARDING) {
				message = this.DISCARD_MESSAGE;
				enableButton = false;
			}
			else if (state == CatanState.ROBBING) {
				message = this.ROBBER_MESSAGE;
				enableButton = false;
			}

			this.getView().updateGameState(message, enableButton);
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
