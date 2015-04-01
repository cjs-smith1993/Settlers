package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesOfferTrade;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;

/**
 * Moves command create when a user attempts to offer a trade.
 *
 */
public class MovesOfferTradeCommand extends AbstractMovesCommand {
	public static final String type = "offerTrade";

	private ResourceInvoice offer;

	public MovesOfferTradeCommand(String json) {
		DTOMovesOfferTrade dto = (DTOMovesOfferTrade) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesOfferTrade.class);

		PlayerNumber playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		PlayerNumber receiver = PlayerNumber.getPlayerNumber(dto.receiver);

		this.offer = new ResourceInvoice(playerIndex, receiver);
		this.offer.setResource(ResourceType.BRICK, dto.offer.brick);
		this.offer.setResource(ResourceType.ORE, dto.offer.ore);
		this.offer.setResource(ResourceType.SHEEP, dto.offer.sheep);
		this.offer.setResource(ResourceType.WHEAT, dto.offer.wheat);
		this.offer.setResource(ResourceType.WOOD, dto.offer.wood);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesOfferTrade(this.offer, this.getGameId(), this.getPlayerId());
	}

}
