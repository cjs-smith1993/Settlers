package client.frontend.turntracker;

import client.frontend.base.*;

/**
 * Interface for the turn tracker controller
 */
public interface ITurnTrackerController extends IController {
	
	/**
	 * This is called when the local player ends their turn
	 */
	void endTurn();
}