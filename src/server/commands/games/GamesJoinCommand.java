package server.commands.games;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGamesJoin;
import shared.definitions.CatanColor;
import shared.model.CatanException;

/**
 * Games command created when the user attempts to join a game
 *
 */
public class GamesJoinCommand extends AbstractGamesCommand {

	private static final String SUCCESS_MESSAGE = "Success";

	private int id;
	private CatanColor color;

	public GamesJoinCommand(String json) {
		DTOGamesJoin dto = (DTOGamesJoin) CatanSerializer.getInstance().deserializeObject(json,
				DTOGamesJoin.class);
		this.id = dto.id;
		this.color = dto.color;

		if (this.color == null) {
			throw new JsonParseException("JSON parse error");
		}
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
		GameCertificate gameCert = null;

		try {
			gameCert = cortex.gamesJoin(this.id, this.color);
			body = SUCCESS_MESSAGE;
			status = StatusCode.OK;
			contentType = ContentType.PLAIN_TEXT;
		} catch (CatanException | ServerException e) {
			body = e.getMessage();
			status = StatusCode.INVALID_REQUEST;
			contentType = ContentType.PLAIN_TEXT;
		}

		response = new CommandResponse(body, status, contentType);
		response.setGameCert(gameCert);
		return response;
	}

}
