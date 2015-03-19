package server.handlers;

import server.commands.ICommand;
import server.factories.GamesCommandFactory;

/**
 * The HttpHandler for all "/games/" calls to the server
 *
 */
public class GamesHandler extends AbstractHandler {

	@Override
	/**
	 * {@inheritDoc}
	 */
	protected ICommand getCommand(String commandName, String json) {
		return GamesCommandFactory.getInstance().getCommand(commandName, json);
	}

}
