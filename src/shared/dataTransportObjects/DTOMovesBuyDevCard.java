package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesBuyDevCard {
	String type = "buyDevCard";
	int playerIndex;

	public DTOMovesBuyDevCard(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex.getInteger();
	}
}
