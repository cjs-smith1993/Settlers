package server.commands.moves;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.core.ICortex;
import server.util.CommandResponse;

/**
 * Moves command created when a user attempts to play a Year of Plenty card.
 *
 */
public class MovesYearOfPlentyCommand extends AbstractMovesCommand {

	public MovesYearOfPlentyCommand(String json, ICortex cortex) {
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
