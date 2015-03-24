package server.handlers;

import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ICommand;
import server.commands.games.GamesJoinCommand;
import server.factories.GamesCommandFactory;
import server.util.CookieConverter;

/**
 * The HttpHandler for all "/games/" calls to the server
 *
 */
public class GamesHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ICommand getCommand(String commandName, String json) {
		return GamesCommandFactory.getInstance().getCommand(commandName, json);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected CommandResponse processCommand(ICommand command, String cookieString) {
		CommandResponse response = null;

		if (command instanceof GamesJoinCommand) {
			UserCertificate userCert = CookieConverter.parseUserCookie(cookieString);
			boolean authenticatedUser = command.authenticateUser(userCert);
			if (authenticatedUser) {
				((GamesJoinCommand) command).setPlayerId(userCert.getUserId());
				response = command.execute();
			}
			else {
				response = CommandResponse.getUnauthenticatedUserResponse();
			}
		}
		else {
			response = command.execute();
		}

		return response;
	}

}
