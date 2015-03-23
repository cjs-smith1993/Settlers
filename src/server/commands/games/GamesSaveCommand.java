package server.commands.games;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOGamesSave;

/**
 * Games command created when the user attempts to save a game
 *
 */
public class GamesSaveCommand extends AbstractGamesCommand {

	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAILURE_MESSAGE = "Failed to save game";

	private int id;
	private String fileName;

	public GamesSaveCommand(String json) {
		DTOGamesSave dto = (DTOGamesSave) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesSave.class);
		this.id = dto.id;
		this.fileName = dto.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
