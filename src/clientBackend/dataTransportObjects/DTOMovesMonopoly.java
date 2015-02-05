package clientBackend.dataTransportObjects;

public class DTOMovesMonopoly {
	String type = "Monopoly";
	String resource;
	int playerIndex;
	
	public DTOMovesMonopoly(String resource, int playerIndex) {
		this.resource = resource;
		this.playerIndex = playerIndex;
	}
}