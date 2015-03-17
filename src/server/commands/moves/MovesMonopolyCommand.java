package server.commands.moves;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.core.ICortex;

/**
 * Moves command created when a user attempts to play a monopoly card.
 *
 */
public class MovesMonopolyCommand extends AbstractMovesCommand {

	public MovesMonopolyCommand(String json, ICortex cortex) {
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
