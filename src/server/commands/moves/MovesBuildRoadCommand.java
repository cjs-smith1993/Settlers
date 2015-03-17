package server.commands.moves;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.core.ICortex;
import server.util.CommandResponse;

/**
 * Moves command created when a user attempts to build a road.
 *
 */
public class MovesBuildRoadCommand extends AbstractMovesCommand {

	public MovesBuildRoadCommand(String json, ICortex cortex) {
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
