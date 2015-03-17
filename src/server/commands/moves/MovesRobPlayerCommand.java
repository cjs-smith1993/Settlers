package server.commands.moves;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.core.ICortex;

/**
 * Moves command created when a user attempts to rob another player.
 *
 */
public class MovesRobPlayerCommand extends AbstractMovesCommand {

	public MovesRobPlayerCommand(String json, ICortex cortex) {
		super(cortex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticate(UserCertificate userCert, GameCertificate gameCert) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}
