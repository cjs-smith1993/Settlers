package server.persistence;

import java.util.Collection;

import server.commands.moves.AbstractMovesCommand;

/**
 * This is an interface for accessing a database of commands.
 * The Central cortex will be calling these functions to gain access
 * to the database. 
 *
 */
public interface ICommandDAO {
	
	/**
	 * This is the method that will return a list of commands
	 * for a given gameId. The commands will only be for the 
	 * version number and after.
	 * @param gameId - the Id of the game you want the commands for
	 * @param version - the version of the current game or 0 for the whole history
	 * @return this is the collection of commands that will return the game to its current state
	 */
	public Collection<AbstractMovesCommand> getCommands (int gameId, int version);
	
	/**
	 * This will add the given command to the list of commands.
	 * It will be stored with the given game id and the version.
	 * @param gameId - the id of the game that the cammand is associated with
	 * @param command - the command to store
	 * @param version - the version of the game that is stored in the database
	 * @return the Id of the command
	 */
	public int createCommand (int gameId, AbstractMovesCommand command, int version);

}
