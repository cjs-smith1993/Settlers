package clientBackend.dataTransportObjects;

public class DTOMovesMonument {
	String type = "Monument";
	int playerIndex;
	
	public DTOMovesMonument(int playerIndex) {
		this.playerIndex = playerIndex;
	}
}
