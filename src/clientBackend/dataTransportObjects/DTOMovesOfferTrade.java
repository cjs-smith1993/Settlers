package clientBackend.dataTransportObjects;

public class DTOMovesOfferTrade {
	String type = "offerTrade";
	int playerIndex;
	DTOOffer offer;
	int receiver;

	public DTOMovesOfferTrade(int playerIndex, int brick, int ore, int sheep, int wheat, int wood,
			int receiver) {
		this.playerIndex = playerIndex;

		this.offer = new DTOOffer(brick, ore, sheep, wheat, wood);

		this.receiver = receiver;
	}
}
