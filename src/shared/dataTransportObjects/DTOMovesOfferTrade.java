package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.PlayerNumber;
import shared.model.ResourceInvoice;

public class DTOMovesOfferTrade {
	public String type = "offerTrade";
	public int playerIndex;
	public DTOOffer offer;
	public int receiver;

	public DTOMovesOfferTrade(ResourceInvoice invoice) {
		this.playerIndex = invoice.getSourcePlayer().getInteger();
		this.offer = new DTOOffer(
				invoice.getBrick(),
				invoice.getOre(),
				invoice.getSheep(),
				invoice.getWheat(),
				invoice.getWood());
		this.receiver = invoice.getDestinationPlayer().getInteger();
	}

	public DTOMovesOfferTrade(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood,
			PlayerNumber receiver) {

		if (playerIndex == null || receiver == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
		this.offer = new DTOOffer(brick, ore, sheep, wheat, wood);
		this.receiver = receiver.getInteger();
	}
}
