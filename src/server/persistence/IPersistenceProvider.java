package server.persistence;

/**
 * The interface that allows the user to interact with the database.
 */
public interface IPersistenceProvider {
	/**
	 * This method will begin a transaction on the database, if the database
	 * supports the action.
	 */
	public void startTransaction();

	/**
	 * This method will end a transaction in progress, on the database, if any
	 * exist.
	 */
	public void endTransaction();

	/**
	 * This returns the proper IUserDAO for the database implementation.
	 * 
	 * @return {@link IUserDAO}
	 */
	public IUserDAO getUserDAO();

	/**
	 * This returns the proper IGameDAO for the database implementation.
	 * 
	 * @return {@link IGameDAO}
	 */
	public IGameDAO getGameDAO();

	/**
	 * This returns the proper ICommandDAO for the database implementation.
	 * 
	 * @return {@link ICommandDAO}
	 */
	public ICommandDAO getCommandDAO();
}
