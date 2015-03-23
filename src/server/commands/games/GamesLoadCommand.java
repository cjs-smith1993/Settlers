package server.commands.games;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGamesLoad;
import shared.model.CatanException;

/**
 * Games command created when the user attempts to load a game
 *
 */
public class GamesLoadCommand extends AbstractGamesCommand {

	private static final String SUCCESS_MESSAGE = "Success";

	private String fileName;

	public GamesLoadCommand(String json) {
		DTOGamesLoad dto = (DTOGamesLoad) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesLoad.class);
		this.fileName = dto.name;
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
			boolean success = cortex.gamesLoad(this.fileName);
			body = CommandResponse.getSuccessMessage();
			status = StatusCode.OK;
			contentType = ContentType.PLAIN_TEXT;
		} catch (CatanException | ServerException e) {
			body = e.getMessage();
			status = StatusCode.INVALID_REQUEST;
			contentType = ContentType.PLAIN_TEXT;
		}

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
