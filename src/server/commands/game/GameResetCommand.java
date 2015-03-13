package server.commands.game;

import shared.model.User;

/**
 *	This is the command to reset a given game.
 *		This is to test as well as to restart a game.
 */

public class GameResetCommand implements IGameCommand {

	/**
     * {@inheritDoc}
     */
	@Override
	public void execute(User user, int gameId, String json) {
		
	}

}
