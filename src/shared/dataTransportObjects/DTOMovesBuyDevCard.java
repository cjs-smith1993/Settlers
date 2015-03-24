package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesBuyDevCard {
	public String type = "buyDevCard";
	public PlayerNumber playerIndex;

	public DTOMovesBuyDevCard(PlayerNumber playerIndex) {
		this.playerIndex = playerIndex;
	}
}
