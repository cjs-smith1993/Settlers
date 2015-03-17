package server.commands.games;

import server.certificates.UserCertificate;
import server.core.ICortex;
import server.util.CommandResponse;

/**
 * Games command created when the user attempts to join a game
 *
 */
public class GamesJoinCommand extends AbstractGamesCommand {

	public GamesJoinCommand(String json, ICortex cortex) {
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
