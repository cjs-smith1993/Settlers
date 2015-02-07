package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRollNumber {
	String type = "rollNumber";
	int playerIndex;
	int number;
	
	public DTOMovesRollNumber(int playerIndex, int number) {
		this.playerIndex = playerIndex;
		this.number = number;
	}
}
