package server.commands.moves;

import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.AbstractCommand;
import server.commands.CommandResponse;
import server.core.CortexFactory;
import shared.model.CatanException;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /moves/
 */
public abstract class AbstractMovesCommand extends AbstractCommand {

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
	 * Authenticates the command given a UserCertificate and GameCertificate
	 *
	 * @param userCert
	 *            a certificate authenticating the user
	 * @param gameCert
	 *            a certificate authenticating the game
	 * @throws CatanException
	 *             TODO
	 * @throws ServerException
	 *             TODO
	 */
	public void authenticate(UserCertificate userCert, GameCertificate gameCert)
			throws CatanException, ServerException {
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse executeInner() throws CatanException, ServerException;
}