package server.factories;

import server.commands.user.AbstractUserCommand;
import server.commands.user.UserLoginCommand;
import server.commands.user.UserRegisterCommand;
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
	 * @return
	 */
	public AbstractUserCommand getCommand(String type, String json) {
		AbstractUserCommand cmd = null;
		ICortex cortex = CortexFactory.getInstance().getCortex();

		switch (type) {
		case "login":
			cmd = new UserLoginCommand(json, cortex);
			break;
		case "register":
			cmd = new UserRegisterCommand(json, cortex);
			break;
		}
		return cmd;
	}
}
