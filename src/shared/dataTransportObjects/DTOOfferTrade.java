package shared.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;

public class DTOOfferTrade {
	public String type = "offerTrade";
	public PlayerNumber playerIndex;
	public Map<String, Object> offer;
	public PlayerNumber receiver;

	public DTOOfferTrade(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood,
			PlayerNumber receiver) {

		this.playerIndex = playerIndex;
		this.offer = new HashMap<>();
		this.offer.put("brick", brick);
		this.offer.put("ore", ore);
		this.offer.put("sheep", sheep);
		this.offer.put("wheat", wheat);
		this.offer.put("wood", wood);
		this.receiver = receiver;
	}
}
