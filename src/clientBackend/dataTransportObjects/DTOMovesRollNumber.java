package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRollNumber {
	String type = "rollNumber";
	PlayerNumber playerIndex;
	int number;

	public DTOMovesRollNumber(PlayerNumber playerIndex, int number) {
		this.playerIndex = playerIndex;
		this.number = number;
	}
}
