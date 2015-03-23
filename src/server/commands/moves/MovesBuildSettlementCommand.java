package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesBuildSettlement;
import shared.dataTransportObjects.DTOVertexLocation;

/**
 * Moves command created when a user attempts to build a settlement.
 *
 */
public class MovesBuildSettlementCommand extends AbstractMovesCommand {

	private int playerIndex;
	private DTOVertexLocation vertexLocation;
	private Boolean free = false;

	public MovesBuildSettlementCommand(String json) {
		DTOMovesBuildSettlement dto = (DTOMovesBuildSettlement) CatanSerializer.getInstance()
				.deserializeObject(json,
						DTOMovesBuildSettlement.class);
		this.playerIndex = dto.playerIndex;
		this.vertexLocation = dto.vertexLocation;
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
