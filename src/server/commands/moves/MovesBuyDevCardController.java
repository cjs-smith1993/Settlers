package server.commands.moves;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

/**
 * Moves commend created when a user attempts to purchase a development card.
 *
 */
public class MovesBuyDevCardController implements IMovesCommand {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCertificate user, GameCertificate game, String json) {
		return null;
	}

}
