package server.commands.user;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ICommand;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /user/
 */
public abstract class AbstractUserCommand implements ICommand {

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse execute();

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
}