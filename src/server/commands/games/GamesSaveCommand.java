package server.commands.games;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGamesSave;
import shared.model.CatanException;

/**
 * Games command created when the user attempts to save a game
 *
 */
public class GamesSaveCommand extends AbstractGamesCommand {

	private int id;
	private String fileName;

	public GamesSaveCommand(String json) {
		DTOGamesSave dto = (DTOGamesSave) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesSave.class);
		this.id = dto.id;
		this.fileName = dto.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		CommandResponse response = null;

		cortex.gamesSave(this.id, this.fileName);
		String body = CommandResponse.getSuccessMessage();
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.PLAIN_TEXT;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
