package server.commands.moves;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOMovesBuyDevCard;
import shared.dataTransportObjects.DTOSaveLoad;

/**
 * Moves commend created when a user attempts to purchase a development card.
 *
 */
public class MovesBuyDevCardCommand extends AbstractMovesCommand {

	private static final String FAILURE_MESSAGE_RES = "Failed to buy Development card - not enough resources";
	
	private int playerIndex;
	
	public MovesBuyDevCardCommand(String json) {
		DTOMovesBuyDevCard dto = (DTOMovesBuyDevCard) CatanSerializer.getInstance().deserializeObject(json,
				DTOMovesBuyDevCard.class);
		this.playerIndex = dto .playerIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
