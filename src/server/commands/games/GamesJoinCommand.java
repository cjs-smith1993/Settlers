package server.commands.games;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOGamesJoin;
import shared.definitions.CatanColor;

/**
 * Games command created when the user attempts to join a game
 *
 */
public class GamesJoinCommand extends AbstractGamesCommand {

	private int id;
	private CatanColor color;

	public GamesJoinCommand(String json) {
		DTOGamesJoin dto = (DTOGamesJoin) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesJoin.class);
		this.id = dto.id;
		this.color = dto.color;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
