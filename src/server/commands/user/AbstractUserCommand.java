package server.commands.user;

import server.CommandResponse;
import server.commands.ICommand;
import server.core.ICortex;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /user/
 */
public abstract class AbstractUserCommand implements ICommand {

	protected ICortex cortex;

	public AbstractUserCommand(ICortex cortex) {
		this.cortex = cortex;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse execute();
}