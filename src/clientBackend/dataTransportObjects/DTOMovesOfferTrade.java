package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOMovesOfferTrade {
	String type = "offerTrade";
	int playerIndex;
	Map<String, Object> offer;
	int receiver;

	public DTOMovesOfferTrade(int playerIndex, int brick, int ore, int sheep, int wheat, int wood,
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
