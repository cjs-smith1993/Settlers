package client.backend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMonopoly {
	String type = "Monopoly";
	ResourceType resource;
	int playerIndex;

	public DTOMovesMonopoly(ResourceType resource, PlayerNumber playerIndex) {
		this.resource = resource;
		this.playerIndex = playerIndex.getInteger();
	}
}