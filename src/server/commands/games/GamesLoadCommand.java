package server.commands.games;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOGamesCreate;
import shared.dataTransportObjects.DTOSaveLoad;

/**
 * Games command created when the user attempts to load a game
 *
 */
public class GamesLoadCommand extends AbstractGamesCommand {
	
	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAILURE_MESSAGE = "Failed to load game - no file by that name.";
	
	private String fileName;

	public GamesLoadCommand(String json) {
		DTOSaveLoad dto = (DTOSaveLoad) CatanSerializer.getInstance().deserializeObject(json,
				DTOSaveLoad.class);
		this.fileName = dto.fileName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
