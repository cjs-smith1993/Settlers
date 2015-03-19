package server.handlers;

import server.commands.ICommand;
import server.factories.MovesCommandFactory;

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

}