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
import shared.model.CatanException;

/**
 * Games command created when the user attempts to create a game
 *
 */
public class GamesCreateCommand extends AbstractGamesCommand {

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
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		CommandResponse response = null;

		DTOGame game = cortex.gamesCreate(this.randomTiles, this.randomNumbers,
				this.randomPorts, this.name);

		String body = CatanSerializer.getInstance().serializeObject(game);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
