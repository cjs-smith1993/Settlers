package server.commands.games;

import server.CommandResponse;
import server.cookies.UserCookie;

/**
 * Games command created when the user attempts to save a game
 *
 */
public class GamesSaveCommand implements IGamesCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCookie user, String json) {
		return null;
	}

}
