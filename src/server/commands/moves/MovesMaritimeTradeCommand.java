package server.commands.moves;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.core.ICortex;

/**
 * Moves command created when a user attempts to use Maritime Trade.
 *
 */
public class MovesMaritimeTradeCommand extends AbstractMovesCommand {

	public MovesMaritimeTradeCommand(String json, ICortex cortex) {
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
