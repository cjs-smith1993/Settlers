package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesFinishTurn {
	String type = "finishTurn";
	PlayerNumber playerIndex;

	public DTOMovesFinishTurn(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex;
	}
}