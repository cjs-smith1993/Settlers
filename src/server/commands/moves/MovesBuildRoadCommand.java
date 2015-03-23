package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOEdgeLocation;
import shared.dataTransportObjects.DTOMovesBuildRoad;
import shared.dataTransportObjects.DTOSaveLoad;

/**
 * Moves command created when a user attempts to build a road.
 *
 */
public class MovesBuildRoadCommand extends AbstractMovesCommand {

	private static final String FAILURE_MESSAGE_RES = "Failed ot build road - not enough resources";
	
	public int playerIndex;
	public Boolean free = false;
	public DTOEdgeLocation roadLocation;
	
	public MovesBuildRoadCommand(String json) {
		DTOMovesBuildRoad dto = (DTOMovesBuildRoad) CatanSerializer.getInstance().deserializeObject(json,
				DTOMovesBuildRoad.class);
		this.playerIndex = dto .playerIndex;
		this.free = dto.free;
		this.roadLocation = dto.roadLocation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
