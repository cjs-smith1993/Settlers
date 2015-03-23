package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesBuildSettlement;
import shared.dataTransportObjects.DTOSaveLoad;
import shared.dataTransportObjects.DTOVertexLocation;

/**
 * Moves command created when a user attempts to build a settlement.
 *
 */
public class MovesBuildSettlementCommand extends AbstractMovesCommand {

	private static final String FAILURE_MESSAGE_RES = "Failed ot build settlement - not enough resources";
	
	public int playerIndex;
	public DTOVertexLocation vertexLocation;
	public Boolean free = false;
	
	public MovesBuildSettlementCommand(String json) {
		DTOMovesBuildSettlement dto = (DTOMovesBuildSettlement) CatanSerializer.getInstance().deserializeObject(json,
				DTOMovesBuildSettlement.class);
		this.playerIndex = dto.playerIndex;
		this.free = dto.free;
		this.vertexLocation = dto.vertexLocation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
