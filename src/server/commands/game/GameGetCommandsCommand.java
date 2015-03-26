package server.commands.game;

import java.util.Collection;

import server.commands.CommandResponse;
import server.commands.ContentType;
import server.commands.ICommand;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.model.CatanException;
import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;

public class GameGetCommandsCommand extends AbstractGameCommand {

	public GameGetCommandsCommand(String json) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();

		CommandResponse response = null;
		Collection<ICommand> model = cortex.gameCommands(this.getGameId());
		String body = CatanSerializer.getInstance().serializeObject(model);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;

		response = new CommandResponse(body, status, contentType);
		return response;
	}
}
