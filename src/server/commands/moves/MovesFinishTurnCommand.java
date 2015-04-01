package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesFinishTurn;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to finish his turn.
 *
 */
public class MovesFinishTurnCommand extends AbstractMovesCommand {
	public static final String type = "finishTurn";

	private PlayerNumber playerIndex;

	public MovesFinishTurnCommand(String json) {
		DTOMovesFinishTurn dto = (DTOMovesFinishTurn) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesFinishTurn.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesFinishTurn(this.playerIndex, this.getGameId(), this.getPlayerId());
	}

}
