package server.handlers;

import server.commands.ICommand;
import server.factories.GameCommandFactory;

/**
 * The HttpHandler for all "/games/" calls to the server
 *
 */
public class GameHandler extends AbstractHandler {

	@Override
	/**
	 * {@inheritDoc}
	 */
	protected ICommand getCommand(String commandName, String json) {
		return GameCommandFactory.getInstance().getCommand(commandName, json);
	}

}
