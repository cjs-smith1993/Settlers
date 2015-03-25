package server.commands.moves;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesBuildCity;
import shared.definitions.PlayerNumber;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to build a city.
 *
 */
public class MovesBuildCityCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;
	private VertexLocation vertexLocation;

	public MovesBuildCityCommand(String json) {
		DTOMovesBuildCity dto = (DTOMovesBuildCity) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesBuildCity.class);

		if (dto.vertexLocation.direction == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		HexLocation hex = new HexLocation(dto.vertexLocation.x, dto.vertexLocation.y);
		VertexDirection dir = dto.vertexLocation.direction;
		this.vertexLocation = new VertexLocation(hex, dir);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesBuildCity(this.playerIndex, this.vertexLocation, this.getGameId(), this.getPlayerId());
	}

}
