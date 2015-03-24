package server.commands.moves;

import java.util.Map;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesDiscardCards;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to discard cards.
 *
 */
public class MovesDiscardCardsCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;
	private Map<ResourceType, Integer> discardedCards;

	public MovesDiscardCardsCommand(String json) {
		DTOMovesDiscardCards dto = (DTOMovesDiscardCards) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesDiscardCards.class);

		if (dto.playerIndex == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = dto.playerIndex;
		this.discardedCards = dto.discardedCards;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesDiscardCards(this.playerIndex, this.discardedCards);
	}

}
