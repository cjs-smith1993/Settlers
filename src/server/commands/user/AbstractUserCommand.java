package server.commands.user;

import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.AbstractCommand;
import server.commands.CommandResponse;
import shared.model.CatanException;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /user/
 */
public abstract class AbstractUserCommand extends AbstractCommand {

	/**
	 * {@inheritDoc}
	 */
	public boolean authenticateUser(UserCertificate userCert) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean authenticateGame(GameCertificate gameCert) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse executeInner() throws CatanException, ServerException;

}