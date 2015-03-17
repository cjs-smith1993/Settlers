package server.commands.moves;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.ICommand;
import server.core.ICortex;
import server.util.CommandResponse;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /moves/
 */
public abstract class AbstractMovesCommand implements ICommand {

	protected ICortex cortex;

	public AbstractMovesCommand(ICortex cortex) {
		this.cortex = cortex;
	}

	/**
	 * Authenticates the command given a UserCertificate and GameCertificate
	 * 
	 * @param userCert
	 *            a certificate authenticating the user
	 * @param gameCert
	 *            a certificate authenticating the game
	 * @return
	 */
	public abstract boolean authenticate(UserCertificate userCert, GameCertificate gameCert);

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse execute();
}