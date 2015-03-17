package server.factories;

import server.commands.user.AbstractUserCommand;

/**
 * A factory for creating instances of IGamesCommand corresponding to a given
 * request. The factory uses a string to determine the appropriate command type,
 * then returns an instance of that type.
 */
public class UserCommandFactory {

	private static UserCommandFactory instance;

	private UserCommandFactory() {

	}

	public static UserCommandFactory getInstance() {
		if (instance == null) {
			instance = new UserCommandFactory();
		}
		return instance;
	}

	/**
	 * Parses the given type and returns an instance of a corresponding command
	 *
	 * @param type
	 *            A String containing the type of the command
	 * @return
	 */
	public static AbstractUserCommand getCommand(String type) {
		return null;
	}
}
