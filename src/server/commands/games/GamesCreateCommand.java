package server.commands.games;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOGamesCreate;
import shared.dataTransportObjects.DTOUserLogin;
import shared.model.CatanException;

/**
 * Games command created when the user attempts to create a game
 *
 */
public class GamesCreateCommand extends AbstractGamesCommand {
	
	private static final String FAILURE_MESSAGE = "Failed to create game - Name field empty.";
	
	private String name;
	private boolean randomTiles;
	private boolean randomPorts;
	private boolean randomNumbers;

	public GamesCreateCommand(String json) {
		DTOGamesCreate dto = (DTOGamesCreate) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesCreate.class);
		this.name = dto.name;
		this.randomNumbers = dto.randomNumbers;
		this.randomPorts = dto.randomPorts;
		this.randomTiles = dto.randomTiles;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		CommandResponse response = null;
		String body;
		StatusCode status;
		ContentType contentType;
			
		try {
			
			
			DTOGame game = cortex.gamesCreate(randomTiles, randomNumbers, randomPorts, name);
			
			body = CatanSerializer.getInstance().serializeObject(game);
			status = StatusCode.OK;
			contentType = ContentType.JSON;
			
			
		} catch (CatanException | ServerException e) {
			body = FAILURE_MESSAGE;
			status = StatusCode.INVALID_REQUEST;
			contentType = ContentType.PLAIN_TEXT;
			e.printStackTrace();
			response = new CommandResponse(body, status, contentType);
			return response;
			
		}
		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
