package shared.dataTransportObjects;

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

		this.playerIndex = playerIndex.getInteger();
		this.resource1 = resource1;
		this.resource2 = resource2;
	}
}