package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRollNumber {
	public String type = "rollNumber";
	public PlayerNumber playerIndex;
	public int number;

	public DTOMovesRollNumber(PlayerNumber playerIndex, int number) {
		this.playerIndex = playerIndex;
		this.number = number;
	}
}
