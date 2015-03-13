package server.commands.game;

import shared.model.User;

/**
 * Game command created when the user attempts to reset the current game
 *
 */

public class GameResetCommand implements IGameCommand {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(User user, int gameId, String json) {

	}

}
