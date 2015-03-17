package server.commands.user;

import server.CommandResponse;
import server.core.ICortex;

/**
 * This is the command class that will register a new user as well as validate
 * and log them in.
 */
public class UserRegisterCommand extends AbstractUserCommand {

	public UserRegisterCommand(String json, ICortex cortex) {
		super(cortex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		return null;
	}

}