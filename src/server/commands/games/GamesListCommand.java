package server.commands.games;

import server.CommandResponse;
import server.certificates.UserCertificate;
import server.core.ICortex;

/**
 * Games command created when the user attempts to get a list of games
 *
 */
public class GamesListCommand extends AbstractGamesCommand {

	public GamesListCommand(String json, ICortex cortex) {
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
