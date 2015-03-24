package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesFinishTurn {
	public String type = "finishTurn";
	public PlayerNumber playerIndex;

	public DTOMovesFinishTurn(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex;
	}
}