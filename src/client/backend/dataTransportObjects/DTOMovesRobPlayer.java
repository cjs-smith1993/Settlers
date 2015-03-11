package client.backend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRobPlayer {
	String type = "robPlayer";
	int playerIndex;
	int victimIndex;
	DTOHexLocation location;

	public DTOMovesRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex, int x, int y) {
		this.playerIndex = playerIndex.getInteger();
		this.victimIndex = victimIndex.getInteger();

		this.location = new DTOHexLocation(x, y);
	}
}
