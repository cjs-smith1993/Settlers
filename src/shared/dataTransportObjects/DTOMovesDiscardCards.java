package shared.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;

public class DTOMovesDiscardCards {
	String type = "discardCards";
	int playerIndex;
	Map<String, Object> discardedCards;

	public DTOMovesDiscardCards(PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood) {
		this.playerIndex = playerIndex.getInteger();

		this.discardedCards = new HashMap<>();
		this.discardedCards.put("brick", brick);
		this.discardedCards.put("ore", ore);
		this.discardedCards.put("sheep", sheep);
		this.discardedCards.put("wheat", wheat);
		this.discardedCards.put("wood", wood);
	}
}
