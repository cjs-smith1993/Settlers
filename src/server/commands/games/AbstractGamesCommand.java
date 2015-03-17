package server.commands.games;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.ICommand;
import server.core.ICortex;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /games/
 */
public abstract class AbstractGamesCommand implements ICommand {

	protected ICortex cortex;

	public AbstractGamesCommand(ICortex cortex) {
		this.cortex = cortex;
	}

	/**
	 * Authenticates the command given a UserCertificate and GameCertificate
	 *
	 * @param userCert
	 *            a certificate authenticating the user
	 * @return
	 */
	public abstract boolean authenticate(UserCertificate userCert);

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse execute();
}