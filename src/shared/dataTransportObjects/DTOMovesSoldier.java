package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;

public class DTOMovesSoldier {
	String type = "Soldier";
	int playerIndex;
	int victimIndex;
	HexLocation location;

	public DTOMovesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex, int x, int y) {
		this.playerIndex = playerIndex.getInteger();
		this.victimIndex = victimIndex.getInteger();

		this.location = new HexLocation(x, y);
	}
}
