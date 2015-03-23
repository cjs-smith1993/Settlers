package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesMonument {
	public String type = "Monument";
	public PlayerNumber playerIndex;

	public DTOMovesMonument(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex;
	}
}
