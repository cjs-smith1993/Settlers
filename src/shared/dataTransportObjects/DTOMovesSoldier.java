package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;

public class DTOMovesSoldier {
	public String type = "Soldier";
	public int playerIndex;
	public int victimIndex;
	public HexLocation location;

	public DTOMovesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex, int x, int y) {
		if (playerIndex == null || victimIndex == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
		this.victimIndex = victimIndex.getInteger();
		this.location = new HexLocation(x, y);
	}
}
