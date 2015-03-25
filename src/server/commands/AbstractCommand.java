package server.commands;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.util.StatusCode;
import shared.model.CatanException;
import client.serverCommunication.ServerException;

public abstract class AbstractCommand implements ICommand {

	private int playerId;
	private int gameId;

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean authenticateUser(UserCertificate userCert);

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean authenticateGame(GameCertificate gameCert);

	/**
	 * {@inheritDoc}
	 */
	public CommandResponse execute() {
		CommandResponse response = null;

		try {
			response = this.executeInner();
		} catch (CatanException e) {
			String body = e.getMessage();
			StatusCode status = StatusCode.INVALID_REQUEST;
			ContentType contentType = ContentType.PLAIN_TEXT;
			response = new CommandResponse(body, status, contentType);
		} catch (ServerException e) {
			String body = e.getMessage();
			StatusCode status = StatusCode.INTERNAL_ERROR;
			ContentType contentType = ContentType.PLAIN_TEXT;
			response = new CommandResponse(body, status, contentType);
		}

		return response;
	}

	/**
	 * Actually executes the command. If execution fails, an exception is thrown
	 * and handled by execute
	 *
	 * @return a CommandResponse for the command
	 * @throws CatanException
	 * @throws ServerException
	 */
	public abstract CommandResponse executeInner() throws CatanException, ServerException;

	public int getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getGameId() {
		return this.gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

}
