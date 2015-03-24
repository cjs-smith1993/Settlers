package server.commands.moves;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesMonopoly;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to play a monopoly card.
 *
 */
public class MovesMonopolyCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;
	private ResourceType resource;

	public MovesMonopolyCommand(String json) {
		DTOMovesMonopoly dto = (DTOMovesMonopoly) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesMonopoly.class);

		if (dto.resource == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.resource = dto.resource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesMonopoly(this.playerIndex, this.resource);
	}

}
