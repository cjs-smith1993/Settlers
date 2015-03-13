package server.commands.user;

import shared.model.User;

/**
* Represents the notion of executing the appropriate action for a given server
* endpoint that begins with /user/
*/

public interface IUserCommand {
	
	/**
     * Executes the command specified in the provided JSON
     * 
     * @param user
     *            An encapsulation of a user to be verified or added
     */

	public void execute(User user);
	
}
