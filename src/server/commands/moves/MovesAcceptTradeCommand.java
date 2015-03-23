package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesAcceptTrade;
import shared.dataTransportObjects.DTOSaveLoad;

/**
 * Moves command created when a user attempts to accept a trade.
 *
 */
public class MovesAcceptTradeCommand extends AbstractMovesCommand {

	private boolean acceptOffer;
	
	public MovesAcceptTradeCommand(String json) {
		DTOMovesAcceptTrade dto = (DTOMovesAcceptTrade) CatanSerializer.getInstance().deserializeObject(json,
				DTOMovesAcceptTrade.class);
		this.acceptOffer = dto.willAccept;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
