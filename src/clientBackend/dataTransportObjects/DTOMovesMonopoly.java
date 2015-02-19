package clientBackend.dataTransportObjects;

import shared.definitions.ResourceType;

public class DTOMovesMonopoly {
	String type = "Monopoly";
	ResourceType resource;
	int playerIndex;

	public DTOMovesMonopoly(ResourceType resource, int playerIndex) {
		this.resource = resource;
		this.playerIndex = playerIndex;
	}
}