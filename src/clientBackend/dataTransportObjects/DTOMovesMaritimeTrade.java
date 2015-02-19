package clientBackend.dataTransportObjects;

import shared.definitions.ResourceType;

public class DTOMovesMaritimeTrade {
	String type = "maritimeTrade";
	int playerIndex;
	int ratio;
	ResourceType inputResource;
	ResourceType outputResource;

	public DTOMovesMaritimeTrade(int playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) {
		this.playerIndex = playerIndex;
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}
}
