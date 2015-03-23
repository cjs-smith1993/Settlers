package server.commands.game;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Game command created when the user attempts to reset the current game
 *
 */
public class GameResetCommand extends AbstractGameCommand {

	public GameResetCommand(String json) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		CommandResponse response = null;

		TransportModel model = cortex.gameReset();
		String body = CatanSerializer.getInstance().serializeObject(model);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
