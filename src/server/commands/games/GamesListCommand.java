package server.commands.games;

import server.CommandResponse;
import server.cookies.UserCookie;

/**
 * Games command created when the user attempts to get a list of games
 *
 */
public class GamesListCommand implements IGamesCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCookie user, String json) {
		return null;
	}

}
