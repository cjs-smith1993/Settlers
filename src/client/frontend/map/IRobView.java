package client.frontend.map;

import client.frontend.base.*;
import client.frontend.data.*;

/**
 * Interface for the rob view, which lets the user select a player to rob
 */
public interface IRobView extends IOverlayView
{
	
	void setPlayers(RobPlayerInfo[] candidateVictims);
}

