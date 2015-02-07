package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesMonument {
	String type = "Monument";
	PlayerNumber playerIndex;
	
	public DTOMovesMonument(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex;
	}
}
