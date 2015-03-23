package server.commands.moves;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesBuyDevCard;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves commend created when a user attempts to purchase a development card.
 *
 */
public class MovesBuyDevCardCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;

	public MovesBuyDevCardCommand(String json) {
		DTOMovesBuyDevCard dto = (DTOMovesBuyDevCard) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesBuyDevCard.class);

		if (dto.playerIndex == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = dto.playerIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesBuyDevCard(this.playerIndex);
	}

}
