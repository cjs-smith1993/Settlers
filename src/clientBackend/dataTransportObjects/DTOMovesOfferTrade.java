package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesOfferTrade {
	String type = "offerTrade";
	PlayerNumber playerIndex;
	DTOOffer offer;
	PlayerNumber receiver;

	public DTOMovesOfferTrade(PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood,
			PlayerNumber receiver) {
		this.playerIndex = playerIndex;

		this.offer = new DTOOffer(brick, ore, sheep, wheat, wood);

		this.receiver = receiver;
	}
}
