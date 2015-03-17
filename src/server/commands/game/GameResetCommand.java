package server.commands.game;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.core.ICortex;

/**
 * Game command created when the user attempts to reset the current game
 *
 */

public class GameResetCommand extends AbstractGameCommand {

	public GameResetCommand(String json, ICortex cortex) {
		super(cortex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
