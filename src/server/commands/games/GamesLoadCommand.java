package server.commands.games;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGamesLoad;
import shared.model.CatanException;

/**
 * Games command created when the user attempts to load a game
 *
 */
public class GamesLoadCommand extends AbstractGamesCommand {

	private String fileName;

	public GamesLoadCommand(String json) {
		DTOGamesLoad dto = (DTOGamesLoad) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesLoad.class);
		this.fileName = dto.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		CommandResponse response = null;

		cortex.gamesLoad(this.fileName);
		String body = CommandResponse.getSuccessMessage();
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.PLAIN_TEXT;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
