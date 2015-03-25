package server.handlers;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ICommand;
import server.commands.game.AbstractGameCommand;
import server.commands.moves.AbstractMovesCommand;
import server.factories.MovesCommandFactory;
import server.util.CookieConverter;

/**
 * The HttpHandler for all "/games/" calls to the server
 *
 */
public class MovesHandler extends AbstractHandler {

	@Override
	/**
	 * {@inheritDoc}
	 */
	protected ICommand getCommand(String commandName, String json) {
		return MovesCommandFactory.getInstance().getCommand(commandName, json);
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
			if(command instanceof AbstractMovesCommand) {
				((AbstractMovesCommand) command).setGameId(gameCert.getGameId());
				((AbstractMovesCommand) command).setPlayerId(userCert.getUserId());
			}
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