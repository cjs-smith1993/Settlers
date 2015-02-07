package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesFinishTurn {
	String type = "finishTurn";
	int playerIndex;
	
	public DTOMovesFinishTurn(int playerIndex) {
		this.playerIndex = playerIndex;
	}
}