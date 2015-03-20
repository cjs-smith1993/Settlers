package shared.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

public class DTOOfferTrade {
	String type = "offerTrade";
	int playerIndex;
	Map<String, Object> offer;
	int receiver;
	
	public DTOOfferTrade(int playerIndex, int brick, int ore, int sheep, int wheat, int wood, int receiver) {
		this.playerIndex = playerIndex;
		
		offer = new HashMap<>();
		offer.put("brick", brick);
		offer.put("ore", ore);
		offer.put("sheep", sheep);
		offer.put("wheat", wheat);
		offer.put("wood", wood);
		
		this.receiver = receiver;
	}
}
