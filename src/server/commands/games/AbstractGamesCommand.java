package server.commands.games;

import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.AbstractCommand;
import server.commands.CommandResponse;
import server.core.CortexFactory;
import shared.model.CatanException;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /games/
 */
public abstract class AbstractGamesCommand extends AbstractCommand {
	
	private int playerId;

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
		return true;
	}

	public abstract CommandResponse executeInner() throws CatanException, ServerException;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}