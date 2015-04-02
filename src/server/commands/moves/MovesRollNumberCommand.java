package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesRollNumber;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to roll the dice.
 */
public class MovesRollNumberCommand extends AbstractMovesCommand {
	public final String type = "rollNumber";

	private PlayerNumber playerIndex;
	private int number;

	public MovesRollNumberCommand(String json) {
		DTOMovesRollNumber dto = (DTOMovesRollNumber) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesRollNumber.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.number = dto.number;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesRollNumber(this.playerIndex, this.number, this.getGameId(),
				this.getPlayerId());
	}

}
