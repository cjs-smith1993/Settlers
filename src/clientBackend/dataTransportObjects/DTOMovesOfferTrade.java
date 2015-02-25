package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesOfferTrade {
	String type = "offerTrade";
	int playerIndex;
	DTOOffer offer;
	int receiver;

	public DTOMovesOfferTrade(PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood,
			PlayerNumber receiver) {
		this.playerIndex = playerIndex.getInteger();

		this.offer = new DTOOffer(brick, ore, sheep, wheat, wood);

		this.receiver = receiver.getInteger();
	}
}
