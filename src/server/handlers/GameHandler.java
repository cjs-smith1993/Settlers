package server.handlers;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ICommand;
import server.factories.GameCommandFactory;
import server.util.CookieConverter;

/**
 * The HttpHandler for all "/games/" calls to the server
 *
 */
public class GameHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ICommand getCommand(String commandName, String json) {
		return GameCommandFactory.getInstance().getCommand(commandName, json);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected CommandResponse processCommand(ICommand command, String cookieString) {
		CommandResponse response = null;

		UserCertificate userCert = CookieConverter.parseUserCookie(cookieString);
		GameCertificate gameCert = CookieConverter.parseGameCookie(cookieString);
		boolean authenticatedUser = command.authenticateUser(userCert);
		boolean authenticatedGame = command.authenticateGame(gameCert);

		if (authenticatedUser && authenticatedGame) {
			command.setGameId(gameCert.getGameId());
			response = command.execute();
		}
		else if (authenticatedUser) {
			response = CommandResponse.getUnauthenticatedGameResponse();
		}
		else {
			response = CommandResponse.getUnauthenticatedUserResponse();
		}

		return response;
	}

}
