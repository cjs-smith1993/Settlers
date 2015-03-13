package server.commands.moves;

import server.cookies.GameCookie;
import server.cookies.UserCookie;

/**
 * Moves commend created when a user attempts to purchase a development card.
 *
 */
public class MovesBuyDevCardController implements IMovesCommand {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(UserCookie user, GameCookie game, String json) {
		// TODO Auto-generated method stub

	}

}
