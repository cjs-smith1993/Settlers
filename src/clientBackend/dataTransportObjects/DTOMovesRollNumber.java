package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRollNumber {
	String type = "rollNumber";
	int playerIndex;
	int number;

	public DTOMovesRollNumber(PlayerNumber playerIndex, int number) {
		this.playerIndex = playerIndex.getInteger();
		this.number = number;
	}
}
