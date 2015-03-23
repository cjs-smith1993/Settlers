package server.commands.games;

import java.util.Collection;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGame;
import shared.model.CatanException;

/**
 * Games command created when the user attempts to get a list of games
 *
 */
public class GamesListCommand extends AbstractGamesCommand {

	public GamesListCommand(String json) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		CommandResponse response = null;
		String body;
		StatusCode status;
		ContentType contentType;

		try {
			ICortex cortex = CortexFactory.getInstance().getCortex();
			Collection<DTOGame> gamesList = cortex.gamesList();
			body = CatanSerializer.getInstance().serializeObject(gamesList);
			status = StatusCode.OK;
			contentType = ContentType.JSON;
		} catch (CatanException e) {
			body = e.getMessage();
			status = StatusCode.INVALID_REQUEST;
			contentType = ContentType.PLAIN_TEXT;
		} catch (ServerException e) {
			body = e.getMessage();
			status = StatusCode.INTERNAL_ERROR;
			contentType = ContentType.PLAIN_TEXT;
		}

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
