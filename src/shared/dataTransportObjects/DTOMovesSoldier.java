package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;

public class DTOMovesSoldier {
	public String type = "Soldier";
	public PlayerNumber playerIndex;
	public PlayerNumber victimIndex;
	public HexLocation location;

	public DTOMovesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex, int x, int y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;
		this.location = new HexLocation(x, y);
	}
}
