package client.backend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMaritimeTrade {
	String type = "maritimeTrade";
	int playerIndex;
	int ratio;
	ResourceType inputResource;
	ResourceType outputResource;

	public DTOMovesMaritimeTrade(PlayerNumber playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) {
		this.playerIndex = playerIndex.getInteger();
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}
}