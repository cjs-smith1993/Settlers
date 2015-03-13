package server.commands.game;

import server.CommandResponse;
import server.cookies.GameCookie;
import server.cookies.UserCookie;

/**
 * Game command created when the user attempts to get the model of the current
 * game
 *
 */
public class GameModelCommand implements IGameCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCookie user, GameCookie game, String json) {
		return null;
	}

}
