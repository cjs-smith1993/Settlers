package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

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

		if (playerIndex == null || victimIndex == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
		this.victimIndex = victimIndex.getInteger();
		this.location = new DTOHexLocation(x, y);
	}
}
