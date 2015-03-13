package server.commands.game;

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
	public void execute(UserCookie user, GameCookie game, String json) {

	}

}
