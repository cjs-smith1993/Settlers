package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesYearOfPlenty {
	public String type = "Year_of_Plenty";
	public int playerIndex;
	public ResourceType resource1;
	public ResourceType resource2;

	public DTOMovesYearOfPlenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2) {

		if (playerIndex == null || resource1 == null || resource2 == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
		this.resource1 = resource1;
		this.resource2 = resource2;
	}
}