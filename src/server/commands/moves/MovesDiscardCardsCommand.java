package server.commands.moves;

import java.util.Map;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesDiscardCards;

/**
 * Moves command created when a user attempts to discard cards.
 *
 */
public class MovesDiscardCardsCommand extends AbstractMovesCommand {

	private int playerIndex;
	private Map<String, Object> discardedCards;

	public MovesDiscardCardsCommand(String json) {
		DTOMovesDiscardCards dto = (DTOMovesDiscardCards) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesDiscardCards.class);
		this.playerIndex = dto.playerIndex;
		this.discardedCards = dto.discardedCards;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
