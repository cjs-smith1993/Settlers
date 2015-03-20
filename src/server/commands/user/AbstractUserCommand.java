package server.commands.user;

import server.commands.CommandResponse;
import server.commands.ICommand;
import server.core.ICortex;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /user/
 */
public abstract class AbstractUserCommand implements ICommand {

	/**
	 * {@inheritDoc}
	 */
	public abstract CommandResponse execute();

}