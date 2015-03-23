package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesAcceptTrade {
	public String type = "acceptTrade";
	public PlayerNumber playerIndex;
	public Boolean willAccept = false;

	public DTOMovesAcceptTrade(PlayerNumber playerIndex, Boolean willAccept) {
		this.playerIndex = playerIndex;
		this.willAccept = willAccept;
	}
}
