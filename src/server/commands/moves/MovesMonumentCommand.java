package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesMonument;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to play a Monument card.
 *
 */
public class MovesMonumentCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;

	public MovesMonumentCommand(String json) {
		DTOMovesMonument dto = (DTOMovesMonument) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesMonument.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesMonument(this.playerIndex, this.getGameId(), this.getPlayerId());
	}

}
