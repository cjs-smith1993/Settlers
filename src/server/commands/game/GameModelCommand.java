package server.commands.game;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

/**
 * Game command created when the user attempts to get the model of the current
 * game
 *
 */
public class GameModelCommand extends GameCommand {
	
	public GameModelCommand(UserCertificate user, GameCertificate game) {
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
