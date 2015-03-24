package server.commands.moves;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesBuildRoad;
import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to build a road.
 *
 */
public class MovesBuildRoadCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;
	private EdgeLocation roadLocation;
	private Boolean free = false;

	public MovesBuildRoadCommand(String json) {
		DTOMovesBuildRoad dto = (DTOMovesBuildRoad) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesBuildRoad.class);

		if (dto.roadLocation.direction == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		HexLocation hex = new HexLocation(dto.roadLocation.x, dto.roadLocation.y);
		EdgeDirection dir = dto.roadLocation.direction;
		this.roadLocation = new EdgeLocation(hex, dir);
		this.free = dto.free;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesBuildRoad(this.playerIndex, this.roadLocation, this.free);
	}

}
