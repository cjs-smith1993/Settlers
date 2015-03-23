package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOEdgeLocation;
import shared.dataTransportObjects.DTOMovesBuildRoad;

/**
 * Moves command created when a user attempts to build a road.
 *
 */
public class MovesBuildRoadCommand extends AbstractMovesCommand {

	private int playerIndex;
	private DTOEdgeLocation roadLocation;
	private Boolean free = false;

	public MovesBuildRoadCommand(String json) {
		DTOMovesBuildRoad dto = (DTOMovesBuildRoad) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesBuildRoad.class);
		this.playerIndex = dto.playerIndex;
		this.roadLocation = dto.roadLocation;
		this.free = dto.free;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
