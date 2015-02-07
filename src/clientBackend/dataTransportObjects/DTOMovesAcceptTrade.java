package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesAcceptTrade {
	String type = "acceptTrade";
	PlayerNumber playerIndex;
	Boolean willAccept = false;
	
	public DTOMovesAcceptTrade(PlayerNumber playerIndex, Boolean willAccept) {
		this.playerIndex = playerIndex;
		this.willAccept = willAccept;
	}
}
