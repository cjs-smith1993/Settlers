package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesMonopoly {
	public String type = "Monopoly";
	public ResourceType resource;
	public int playerIndex;

	public DTOMovesMonopoly(ResourceType resource, PlayerNumber playerIndex) {
		if (resource == null || playerIndex == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.resource = resource;
		this.playerIndex = playerIndex.getInteger();
	}
}