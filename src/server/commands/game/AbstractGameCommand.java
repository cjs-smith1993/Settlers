package server.commands.game;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ICommand;
import server.core.CortexFactory;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /game/
 */

public abstract class AbstractGameCommand implements ICommand {

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
	public abstract CommandResponse execute();
}