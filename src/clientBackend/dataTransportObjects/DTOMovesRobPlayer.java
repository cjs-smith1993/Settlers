package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRobPlayer {
	String type = "robPlayer";
	PlayerNumber playerIndex;
	PlayerNumber victimIndex;
	DTOHexLocation location;

	public DTOMovesRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex, int x, int y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;

		this.location = new DTOHexLocation(x, y);
	}
}
