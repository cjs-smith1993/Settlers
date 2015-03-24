package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.model.ResourceInvoice;

public class DTOMovesOfferTrade {
	public String type = "offerTrade";
	public PlayerNumber playerIndex;
	public DTOOffer offer;
	public PlayerNumber receiver;

	public DTOMovesOfferTrade(ResourceInvoice invoice) {
		this.playerIndex = invoice.getSourcePlayer();
		this.offer = new DTOOffer(
				invoice.getBrick(),
				invoice.getOre(),
				invoice.getSheep(),
				invoice.getWheat(),
				invoice.getWood());
		this.receiver = invoice.getDestinationPlayer();
	}

	public DTOMovesOfferTrade(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood,
			PlayerNumber receiver) {

		this.playerIndex = playerIndex;
		this.offer = new DTOOffer(brick, ore, sheep, wheat, wood);
		this.receiver = receiver;
	}
}
