package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesBuildCity;
import shared.dataTransportObjects.DTOVertexLocation;

/**
 * Moves command created when a user attempts to build a city.
 *
 */
public class MovesBuildCityCommand extends AbstractMovesCommand {

	private int playerIndex;
	private DTOVertexLocation vertexLocation;

	public MovesBuildCityCommand(String json) {
		DTOMovesBuildCity dto = (DTOMovesBuildCity) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesBuildCity.class);
		this.vertexLocation = dto.vertexLocation;
		this.playerIndex = dto.playerIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
