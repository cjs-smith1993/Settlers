package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesAcceptTrade;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to accept a trade.
 *
 */
public class MovesAcceptTradeCommand extends AbstractMovesCommand {
	public final String type = "acceptTrade";

	private PlayerNumber playerIndex;
	private boolean willAccept;

	public MovesAcceptTradeCommand(String json) {
		DTOMovesAcceptTrade dto = (DTOMovesAcceptTrade) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesAcceptTrade.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.willAccept = dto.willAccept;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesAcceptTrade(this.playerIndex, this.willAccept, this.getGameId(),
				this.getPlayerId());
	}

}
