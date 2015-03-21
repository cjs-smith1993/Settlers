package server.commands.games;

import client.backend.CatanSerializer;
import server.commands.CommandResponse;
import shared.dataTransportObjects.DTOSaveLoad;

/**
 * Games command created when the user attempts to save a game
 *
 */
public class GamesSaveCommand extends AbstractGamesCommand {

	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAILURE_MESSAGE = "Failed to save game - file name format incorrect";
	
	private String fileName;
	
	public GamesSaveCommand(String json) {
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
