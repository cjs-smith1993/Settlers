package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMonopoly {
	String type = "Monopoly";
	ResourceType resource;
	PlayerNumber playerIndex;
	
	public DTOMovesMonopoly(ResourceType resource, PlayerNumber playerIndex) {
		this.resource = resource;
		this.playerIndex = playerIndex;
	}
}