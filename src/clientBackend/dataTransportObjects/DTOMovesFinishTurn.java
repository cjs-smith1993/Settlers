package clientBackend.dataTransportObjects;

public class DTOMovesFinishTurn {
	String type = "finishTurn";
	int playerIndex;
	
	public DTOMovesFinishTurn(int playerIndex) {
		this.playerIndex = playerIndex;
	}
}