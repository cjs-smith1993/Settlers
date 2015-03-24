package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesDiscardCards {
	public String type = "discardCards";
	public PlayerNumber playerIndex;
	public DTOCards discardedCards;

	public DTOMovesDiscardCards(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood) {

		this.playerIndex = playerIndex;
		this.discardedCards = new DTOCards(brick, ore, sheep, wheat, wood);
	}
}
