package server.handlers;

import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ICommand;
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

		UserCertificate userCert = CookieConverter.parseUserCookie(cookieString);
		boolean authenticatedUser = command.authenticateUser(userCert);

		if (authenticatedUser) {
			response = command.execute();
		}
		else {
			response = CommandResponse.getUnauthenticatedUserResponse();
		}

		return response;
	}

}
