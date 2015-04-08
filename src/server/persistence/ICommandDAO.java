package server.persistence;

import java.util.Collection;

import server.commands.moves.AbstractMovesCommand;

/**
 * An interface for querying/modifying the commands stored in the database.
 *
 */
public interface ICommandDAO {

	/**
	 * Return a list of commands for a given gameId with a version strictly
	 * greater than the version provided
	 *
	 * @param gameId
	 *            the Id of the game
	 * @param version
	 *            the version of the current game, or 0 for the entire history
	 * @return the collection of commands that have been executed since the
	 *         given version
	 */
	public Collection<AbstractMovesCommand> getCommands(int gameId, int version);

	/**
	 * Add the given command to the list of commands for the specified gameId
	 * and with the specified version
	 *
	 * @param gameId
	 *            the id of the game that the command is associated with
	 * @param command
	 *            the command to store
	 * @param version
	 *            the version of the game after executing this command
	 * @return the id of the command in the database
	 */
	public int createCommand(int gameId, AbstractMovesCommand command, int version);

}
