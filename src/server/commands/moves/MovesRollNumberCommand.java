package server.commands.moves;

import server.cookies.GameCookie;
import server.cookies.UserCookie;

/**
 * Moves command created when a user attempts to roll the dice.
 */
public class MovesRollNumberCommand implements IMovesCommand {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(UserCookie user, GameCookie game, String json) {
		// TODO Auto-generated method stub

	}
}