package server.commands.moves;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

/**
 * Moves command created when a user attempts to send a chat.
 *
 */
public class MovesSendChatCommand implements IMovesCommand {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute(UserCertificate user, GameCertificate game, String json) {
		return null;
	}

}
