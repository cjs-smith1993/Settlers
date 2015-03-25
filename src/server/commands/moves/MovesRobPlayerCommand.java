package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesRobPlayer;
import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to rob another player.
 *
 */
public class MovesRobPlayerCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;
	private PlayerNumber victimIndex;
	private HexLocation location;

	public MovesRobPlayerCommand(String json) {
		DTOMovesRobPlayer dto = (DTOMovesRobPlayer) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesRobPlayer.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.victimIndex = PlayerNumber.getPlayerNumber(dto.victimIndex);
		this.location = new HexLocation(dto.location.x, dto.location.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesRobPlayer(this.playerIndex, this.victimIndex, this.location, this.getGameId(), this.getPlayerId());
	}

}
