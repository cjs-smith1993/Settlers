package shared.dataTransportObjects;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class DTOMovesDiscardCards {
	public String type = "discardCards";
	public PlayerNumber playerIndex;
	public Map<ResourceType, Integer> discardedCards;

	public DTOMovesDiscardCards(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood) {

		this.playerIndex = playerIndex;
		this.discardedCards = new HashMap<>();
		this.discardedCards.put(ResourceType.BRICK, brick);
		this.discardedCards.put(ResourceType.ORE, ore);
		this.discardedCards.put(ResourceType.SHEEP, sheep);
		this.discardedCards.put(ResourceType.WHEAT, wheat);
		this.discardedCards.put(ResourceType.WOOD, wood);
	}
}
