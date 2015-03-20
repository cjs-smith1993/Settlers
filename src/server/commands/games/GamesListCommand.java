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
		//		[{'title':'DefaultGame','id':0,'players':[{'color':'orange','name':'Sam','id':0},{'color':'blue','name':'Brooke','id':1},{'color':'red','name':'Pete','id':10},{'color':'green','name':'Mark','id':11}]},{'title':'AIGame','id':1,'players':[{'color':'orange','name':'Pete','id':10},{'color':'puce','name':'Steve','id':-2},{'color':'blue','name':'Squall','id':-3},{'color':'yellow','name':'Quinn','id':-4}]},{'title':'EmptyGame','id':2,'players':[{'color':'orange','name':'Sam','id':0},{'color':'blue','name':'Brooke','id':1},{'color':'red','name':'Pete','id':10},{'color':'green','name':'Mark','id':11}]}]
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
