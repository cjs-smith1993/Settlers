package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesAcceptTrade {
	String type = "acceptTrade";
	int playerIndex;
	Boolean willAccept = false;

	public DTOMovesAcceptTrade(PlayerNumber playerIndex, Boolean willAccept) {
		this.playerIndex = playerIndex.getInteger();
		this.willAccept = willAccept;
	}
}
