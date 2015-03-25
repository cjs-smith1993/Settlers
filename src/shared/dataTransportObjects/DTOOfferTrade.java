package shared.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOOfferTrade {
	public String type = "offerTrade";
	public int playerIndex;
	public Map<String, Object> offer;
	public int receiver;

	public DTOOfferTrade(
			int playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood,
			int receiver) {
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
