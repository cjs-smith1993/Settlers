package server.handlers;

import server.commands.CommandResponse;
import server.commands.ICommand;
import server.factories.UserCommandFactory;

/**
 * The HttpHandler for all "/user/" calls to the server
 *
 */
public class UserHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ICommand getCommand(String commandName, String json) {
		return UserCommandFactory.getInstance().getCommand(commandName, json);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected CommandResponse processCommand(ICommand command, String cookieString) {
		return command.execute();
	}

}
