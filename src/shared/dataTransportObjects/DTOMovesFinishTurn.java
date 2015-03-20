package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesFinishTurn {
	String type = "finishTurn";
	int playerIndex;

	public DTOMovesFinishTurn(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex.getInteger();
	}
}