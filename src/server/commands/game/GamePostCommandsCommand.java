package server.commands.game;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.commands.ICommand;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Game command created when the user attempts to execute a list of commands
 *
 */
public class GamePostCommandsCommand extends AbstractGameCommand {

	Collection<ICommand> commandsList;

	@SuppressWarnings("unchecked")
	public GamePostCommandsCommand(String json) {
		Type objectType = new TypeToken<Collection<ICommand>>() {
		}.getType();
		this.commandsList = (Collection<ICommand>) CatanSerializer.getInstance().deserializeObject(
				json, objectType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();

		CommandResponse response = null;
		TransportModel model = cortex.gameCommands(this.commandsList, this.getGameId());
		String body = CatanSerializer.getInstance().serializeObject(model);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
