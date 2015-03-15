package server.commands.games;

import server.CommandResponse;
import server.certificates.UserCertificate;

/**
 * Games command created when the user attempts to load a game
 *
 */
public class GamesLoadCommand implements IGamesCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCertificate user, String json) {
		return null;
	}

}
