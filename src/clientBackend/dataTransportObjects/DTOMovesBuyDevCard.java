package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesBuyDevCard {
	String type = "buyDevCard";
	PlayerNumber playerIndex;

	public DTOMovesBuyDevCard(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex;
	}
}
