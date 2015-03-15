package server.commands.game;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

/**
 * Game command created when the user attempts to reset the current game
 *
 */

public class GameResetCommand extends GameCommand {

	public GameResetCommand(UserCertificate user, GameCertificate game) {
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
