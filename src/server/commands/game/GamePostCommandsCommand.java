package server.commands.game;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.commands.moves.AbstractMovesCommand;
import server.core.CortexFactory;
import server.factories.MovesCommandFactory;
import server.util.StatusCode;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Game command created when the user attempts to execute a list of commands
 *
 */
public class GamePostCommandsCommand extends AbstractGameCommand {

	Collection<AbstractMovesCommand> commandsList;

	@SuppressWarnings("unchecked")
	public GamePostCommandsCommand(String json) {
		this.commandsList = new ArrayList<AbstractMovesCommand>();
		
		JsonArray o = new JsonParser().parse(json).getAsJsonArray();
		
		for (JsonElement elem : o) {
			JsonObject obj = elem.getAsJsonObject();
			JsonElement typeElem = obj.get("type");
			String type = typeElem.toString().replaceAll("\"", "");
			AbstractMovesCommand command = MovesCommandFactory.getInstance().getCommand(type, obj.toString());
			command.setPlayerId(obj.get("playerId").getAsInt());
			command.setGameId(obj.get("gameId").getAsInt());
			this.commandsList.add(command);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		
		for (AbstractMovesCommand command : commandsList) {
			command.executeInner();
		}
		
		TransportModel model = CortexFactory.getInstance().getCortex().gameModel(-1, this.getGameId());// -1 for no version checking
		String body = CatanSerializer.getInstance().serializeObject(model);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;
		
		return new CommandResponse(body, status, contentType);
	}

}
