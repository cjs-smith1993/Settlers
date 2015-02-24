package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;

public class DTOMovesSoldier {
	String type = "Soldier";
	PlayerNumber playerIndex;
	PlayerNumber victimIndex;
	HexLocation location;

	public DTOMovesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex, int x, int y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;

		this.location = new HexLocation(x, y);
	}
}
