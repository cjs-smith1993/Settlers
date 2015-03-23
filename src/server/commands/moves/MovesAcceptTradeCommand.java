package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesAcceptTrade;

/**
 * Moves command created when a user attempts to accept a trade.
 *
 */
public class MovesAcceptTradeCommand extends AbstractMovesCommand {

	private int playerIndex;
	private boolean willAccept;

	public MovesAcceptTradeCommand(String json) {
		DTOMovesAcceptTrade dto = (DTOMovesAcceptTrade) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesAcceptTrade.class);
		this.playerIndex = dto.playerIndex;
		this.willAccept = dto.willAccept;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
