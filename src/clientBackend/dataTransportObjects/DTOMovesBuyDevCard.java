package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesBuyDevCard {
	String type = "buyDevCard";
	int playerIndex;
	
	public DTOMovesBuyDevCard(int playerIndex) {
		this.playerIndex = playerIndex;
	}
}
