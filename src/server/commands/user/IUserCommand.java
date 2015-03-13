package server.commands.user;

import server.CommandResponse;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /user/
 */
public interface IUserCommand {

	/**
	 * Executes the command specified in the provided JSON
	 *
	 * @param json
	 *            A JSON blob containing the required information for the
	 *            desired command
	 * @return TODO
	 */
	public CommandResponse execute(String json);

}
