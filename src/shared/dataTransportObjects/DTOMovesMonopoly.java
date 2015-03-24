package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMonopoly {
	public String type = "Monopoly";
	public ResourceType resource;
	public int playerIndex;

	public DTOMovesMonopoly(ResourceType resource, PlayerNumber playerIndex) {
		this.resource = resource;
		this.playerIndex = playerIndex.getInteger();
	}
}