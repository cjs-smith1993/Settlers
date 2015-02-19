package clientBackend.dataTransportObjects;

public class DTOMovesAcceptTrade {
	String type = "acceptTrade";
	int playerIndex;
	Boolean willAccept = false;

	public DTOMovesAcceptTrade(int playerIndex, Boolean willAccept) {
		this.playerIndex = playerIndex;
		this.willAccept = willAccept;
	}
}
