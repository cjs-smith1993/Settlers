package client.turntracker;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.PlayerNumber;
import client.base.*;
import client.data.PlayerInfo;
import clientBackend.model.Facade;
import clientBackend.model.Player;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	Facade facade = Facade.getInstance();
	
	public TurnTrackerController(ITurnTrackerView view) {
		super(view);
		facade.addObserver(this);
	}
	
	@Override
	public ITurnTrackerView getView() {
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		
	}

	@Override
	public void update(Observable o, Object arg) {
		ITurnTrackerView view = getView();
		List<PlayerInfo> players = facade.getPlayers();
		
		if (players != null) {
			for (PlayerInfo playerInfo : players) {
				if (playerInfo != null) {
					view.initializePlayer(playerInfo.getPlayerIndex().getInteger(), playerInfo.getName(), playerInfo.getColor());
					
					if (playerInfo.getPlayerIndex() == facade.getClientPlayer()) {
						view.setLocalPlayerColor(playerInfo.getColor());
					}
				}
			}
			
//			PlayerNumber player = facade.getLongestRoadPlayer();
//			PlayerNumber largestArmyPlayer = facade.getLargestArmyPlayer();
		}
	}
}

