package clientBackend.dataTransportObjects;

import shared.locations.HexLocation;

public class DTOMovesSoldier {
	String type = "Soldier";
	int playerIndex;
	int victimIndex;
	HexLocation location;

	public DTOMovesSoldier(int playerIndex, int victimIndex, int x, int y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;

		this.location = new HexLocation(x, y);
	}
}
