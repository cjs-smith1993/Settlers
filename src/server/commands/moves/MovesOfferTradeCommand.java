package server.commands.moves;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

/**
 * Moves command create when a user attempts to offer a trade.
 *
 */
public class MovesOfferTradeCommand implements IMovesCommand {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCertificate user, GameCertificate game, String json) {
		return null;
	}

}
