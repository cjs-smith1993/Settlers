package server.commands.game;

import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.AbstractCommand;
import server.commands.CommandResponse;
import server.core.CortexFactory;
import shared.model.CatanException;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /game/
 */

public abstract class AbstractGameCommand extends AbstractCommand {
	
	private int gameId;

	/**
	 * {@inheritDoc}
	 */
	public boolean authenticateUser(UserCertificate userCert) {
		return CortexFactory.getInstance().getCortex().authenticateUser(userCert);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean authenticateGame(GameCertificate gameCert) {
		return CortexFactory.getInstance().getCortex().authenticateGame(gameCert);
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse executeInner() throws CatanException, ServerException;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	

}