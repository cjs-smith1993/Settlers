package server.commands.game;

import server.CommandResponse;
import server.cookies.GameCookie;
import server.cookies.UserCookie;

/**
 * Game command created when the user attempts to get the model of the current
 * game
 *
 */
public class GameModelCommand extends GameCommand {
	
	public GameModelCommand(UserCookie user, GameCookie game) {
		super(user,game);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
