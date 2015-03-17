package server.factories;

import server.commands.games.*;
import server.core.CortexFactory;
import server.core.ICortex;

/**
 * A factory for creating instances of IGamesCommand corresponding to a given
 * request. The factory uses a string to determine the appropriate command type,
 * then returns an instance of that type.
 */
public class GamesCommandFactory {

	private static GamesCommandFactory instance;

	private GamesCommandFactory() {

	}

	public static GamesCommandFactory getInstance() {
		if (instance == null) {
			instance = new GamesCommandFactory();
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
	public AbstractGamesCommand getCommand(String type, String json) {
		AbstractGamesCommand cmd = null;
		ICortex cortex = CortexFactory.getInstance().getCortex();

		switch (type) {
		case "list":
			cmd = new GamesListCommand(json, cortex);
			break;
		case "create":
			cmd = new GamesCreateCommand(json, cortex);
			break;
		case "join":
			cmd = new GamesJoinCommand(json, cortex);
			break;
		case "save":
			cmd = new GamesSaveCommand(json, cortex);
			break;
		case "load":
			cmd = new GamesLoadCommand(json, cortex);
			break;
		}

		return cmd;
	}
}
