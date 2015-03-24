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
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		CommandResponse response = null;

		Collection<DTOGame> gamesList = cortex.gamesList();
		String body = CatanSerializer.getInstance().serializeObject(gamesList);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
