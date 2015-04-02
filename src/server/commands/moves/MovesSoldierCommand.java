package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesSoldier;
import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to play a Soldier card.
 *
 */
public class MovesSoldierCommand extends AbstractMovesCommand {
	public final String type = "Soldier";

	private PlayerNumber playerIndex;
	private PlayerNumber victimIndex;
	private HexLocation location;

	public MovesSoldierCommand(String json) {
		DTOMovesSoldier dto = (DTOMovesSoldier) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesSoldier.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.victimIndex = PlayerNumber.getPlayerNumber(dto.victimIndex);
		this.location = new HexLocation(dto.location.getX(), dto.location.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesSoldier(this.playerIndex, this.victimIndex, this.location,
				this.getGameId(), this.getPlayerId());
	}

}
