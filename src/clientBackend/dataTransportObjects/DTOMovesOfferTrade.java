package clientBackend.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;

public class DTOMovesOfferTrade {
	String type = "offerTrade";
	PlayerNumber playerIndex;
	Map<String, Object> offer;
	PlayerNumber receiver;
	
	public DTOMovesOfferTrade(PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood, PlayerNumber receiver) {
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
