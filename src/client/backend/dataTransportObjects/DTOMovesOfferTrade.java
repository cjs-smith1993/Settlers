package client.backend.dataTransportObjects;

import shared.definitions.PlayerNumber;
import shared.model.ResourceInvoice;

public class DTOMovesOfferTrade {
	String type = "offerTrade";
	int playerIndex;
	DTOOffer offer;
	int receiver;

	public DTOMovesOfferTrade(ResourceInvoice invoice) {
		this.playerIndex = invoice.getSourcePlayer().getInteger();

		this.offer = new DTOOffer(invoice.getBrick(), invoice.getOre(), invoice.getSheep(), 
				invoice.getWheat(), invoice.getWood());

		this.receiver = invoice.getDestinationPlayer().getInteger();
	}
	
	public DTOMovesOfferTrade(PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood,
			PlayerNumber receiver) {
		this.playerIndex = playerIndex.getInteger();

		this.offer = new DTOOffer(brick, ore, sheep, wheat, wood);

		this.receiver = receiver.getInteger();
	}
}
