package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesMonument {
	String type = "Monument";
	int playerIndex;

	public DTOMovesMonument(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex.getInteger();
	}
}
