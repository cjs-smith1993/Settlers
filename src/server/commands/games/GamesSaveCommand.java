package server.commands.games;

import server.certificates.UserCertificate;
import server.core.ICortex;
import server.util.CommandResponse;

/**
 * Games command created when the user attempts to save a game
 *
 */
public class GamesSaveCommand extends AbstractGamesCommand {

	public GamesSaveCommand(String json, ICortex cortex) {
		super(cortex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticate(UserCertificate userCert) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
