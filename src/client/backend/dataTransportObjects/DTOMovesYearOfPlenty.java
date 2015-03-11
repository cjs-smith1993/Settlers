package client.backend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesYearOfPlenty {
	String type = "Year_of_Plenty";
	int playerIndex;
	ResourceType resource1;
	ResourceType resource2;

	public DTOMovesYearOfPlenty(PlayerNumber playerIndex, ResourceType resource1, ResourceType resource2) {
		this.playerIndex = playerIndex.getInteger();
		this.resource1 = resource1;
		this.resource2 = resource2;
	}
}