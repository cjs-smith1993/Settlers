package clientBackend.dataTransportObjects;

public class DTOMovesRollNumber {
	String type = "rollNumber";
	int playerIndex;
	int number;

	public DTOMovesRollNumber(int playerIndex, int number) {
		this.playerIndex = playerIndex;
		this.number = number;
	}
}
