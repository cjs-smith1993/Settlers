package server.factories;

import server.commands.user.*;
import server.core.CortexFactory;
import server.core.ICortex;

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
	 * @param json
	 *            A JSON blob containing the information for the command
	 * @return
	 */
	public AbstractUserCommand getCommand(String type, String json) {
		AbstractUserCommand cmd = null;

		switch (type) {
		case "login":
			cmd = new UserLoginCommand(json);
			break;
		case "register":
			cmd = new UserRegisterCommand(json);
			break;
		}

		return cmd;
	}
}
