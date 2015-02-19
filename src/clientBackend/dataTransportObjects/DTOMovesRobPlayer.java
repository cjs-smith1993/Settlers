package clientBackend.dataTransportObjects;

public class DTOMovesRobPlayer {
	String type = "robPlayer";
	int playerIndex;
	int victimIndex;
	DTOHexLocation location;

	public DTOMovesRobPlayer(int playerIndex, int victimIndex, int x, int y) {
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;

		this.location = new DTOHexLocation(x, y);
	}
}
