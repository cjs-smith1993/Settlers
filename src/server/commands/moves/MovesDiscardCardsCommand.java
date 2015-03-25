package server.commands.moves;

import java.util.HashMap;
import java.util.Map;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOCards;
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

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		DTOCards cards = dto.discardedCards;
		this.discardedCards = new HashMap<ResourceType, Integer>();
		this.discardedCards.put(ResourceType.BRICK, cards.brick);
		this.discardedCards.put(ResourceType.ORE, cards.ore);
		this.discardedCards.put(ResourceType.SHEEP, cards.sheep);
		this.discardedCards.put(ResourceType.WHEAT, cards.wheat);
		this.discardedCards.put(ResourceType.WOOD, cards.wood);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesDiscardCards(this.playerIndex, this.discardedCards, this.getGameId(), this.getPlayerId());
	}

}
