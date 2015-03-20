package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesAcceptTrade {
	public String type = "acceptTrade";
	public int playerIndex;
	public Boolean willAccept = false;

	public DTOMovesAcceptTrade(PlayerNumber playerIndex, Boolean willAccept) {
		this.playerIndex = playerIndex.getInteger();
		this.willAccept = willAccept;
	}
}
