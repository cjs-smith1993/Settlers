package server.commands.game;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.core.ICortex;

/**
 * Game command created when the user attempts to get the model of the current
 * game
 *
 */
public class GameModelCommand extends AbstractGameCommand {
	
	public GameModelCommand(String json, ICortex cortex) {
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
