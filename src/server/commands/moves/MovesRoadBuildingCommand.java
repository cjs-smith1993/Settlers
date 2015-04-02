package server.commands.moves;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesRoadBuilding;
import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to use a Road Building card.
 *
 */
public class MovesRoadBuildingCommand extends AbstractMovesCommand {
	public final String type = "Road_Building";

	private PlayerNumber playerIndex;
	private EdgeLocation edge1;
	private EdgeLocation edge2;

	public MovesRoadBuildingCommand(String json) {
		DTOMovesRoadBuilding dto = (DTOMovesRoadBuilding) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesRoadBuilding.class);

		if (dto.spot1.direction == null || dto.spot2.direction == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		HexLocation hex1 = new HexLocation(dto.spot1.x, dto.spot1.y);
		EdgeDirection dir1 = dto.spot1.direction;
		this.edge1 = new EdgeLocation(hex1, dir1);
		HexLocation hex2 = new HexLocation(dto.spot2.x, dto.spot2.y);
		EdgeDirection dir2 = dto.spot2.direction;
		this.edge2 = new EdgeLocation(hex2, dir2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesRoadBuilding(this.playerIndex, this.edge1, this.edge2, this.getGameId(),
				this.getPlayerId());
	}

}
