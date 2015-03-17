package server.factories;

import server.commands.moves.AbstractMovesCommand;

/**
 * A factory for creating instances of IMovesCommand corresponding to a given
 * request. The factory uses a string to determine the appropriate command type,
 * then returns an instance of that type.
 */
public class MovesCommandFactory {

	private static MovesCommandFactory instance;

	private MovesCommandFactory() {

	}

	public static MovesCommandFactory getInstance() {
		if (instance == null) {
			instance = new MovesCommandFactory();
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
	public AbstractMovesCommand getCommand(String type) {
		return null;
	}
}
