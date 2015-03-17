package server.commands.user;

import server.core.ICortex;
import server.util.CommandResponse;

/**
 * This is the command in user to validate a previous user
 */
public class UserLoginCommand extends AbstractUserCommand {

	public UserLoginCommand(String json, ICortex cortex) {
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