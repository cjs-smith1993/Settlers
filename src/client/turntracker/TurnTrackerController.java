package client.turntracker;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.PlayerNumber;
import client.base.*;
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
		Map<PlayerNumber, Player> players = facade.getPlayers();
		
		if (players != null) {
			for (Map.Entry<PlayerNumber, Player> entry : players.entrySet()) {
				if (entry.getValue() != null) {
					view.initializePlayer(entry.getKey().getInteger(), entry.getValue().getUser().getName(), entry.getValue().getColor());
					
					if (entry.getKey() == facade.getClientPlayer()) {
						view.setLocalPlayerColor(entry.getValue().getColor());
					}
				}
			}
		}
	}
}

