package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMaritimeTrade {
	public String type = "maritimeTrade";
	public int playerIndex;
	public int ratio;
	public ResourceType inputResource;
	public ResourceType outputResource;

	public DTOMovesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) {

		if (inputResource == null || outputResource == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}
}
