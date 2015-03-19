package server.handlers;

import server.commands.ICommand;
import server.factories.UserCommandFactory;

/**
 * The HttpHandler for all "/user/" calls to the server
 *
 */
public class UserHandler extends AbstractHandler {

	@Override
	/**
	 * {@inheritDoc}
	 */
	protected ICommand getCommand(String commandName, String json) {
		return UserCommandFactory.getInstance().getCommand(commandName, json);
	}

}
