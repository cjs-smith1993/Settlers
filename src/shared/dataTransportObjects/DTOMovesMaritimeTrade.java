package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMaritimeTrade {
	public String type = "maritimeTrade";
	public PlayerNumber playerIndex;
	public int ratio;
	public ResourceType inputResource;
	public ResourceType outputResource;

	public DTOMovesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) {

		this.playerIndex = playerIndex;
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}
}
