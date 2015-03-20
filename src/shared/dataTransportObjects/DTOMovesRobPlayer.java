package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesRobPlayer {
	public String type = "robPlayer";
	public int playerIndex;
	public int victimIndex;
	public DTOHexLocation location;

	public DTOMovesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			int x,
			int y) {
		this.playerIndex = playerIndex.getInteger();
		this.victimIndex = victimIndex.getInteger();
		this.location = new DTOHexLocation(x, y);
	}
}
