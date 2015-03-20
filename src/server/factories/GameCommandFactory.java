package server.factories;

import server.commands.game.*;

/**
 * A factory for creating instances of IGameCommand corresponding to a given
 * request. The factory uses a string to determine the appropriate command type,
 * then returns an instance of that type.
 */
public class GameCommandFactory {

	private static GameCommandFactory instance;

	private GameCommandFactory() {

	}

	public static GameCommandFactory getInstance() {
		if (instance == null) {
			instance = new GameCommandFactory();
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
	public AbstractGameCommand getCommand(String type, String json) {
		AbstractGameCommand cmd = null;

		switch (type) {
		case "model":
			cmd = new GameModelCommand(json);
			break;
		case "reset":
			cmd = new GameResetCommand(json);
			break;
		case "commands":
			cmd = new GameCommandsCommand(json);
			break;
		}

		return cmd;
	}
}
