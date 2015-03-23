package server.commands.games;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOGamesCreate;

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
	public CommandResponse execute() {
		return null;
	}

}
