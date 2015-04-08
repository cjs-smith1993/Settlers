package server.persistence;

import java.util.Collection;

import shared.transport.TransportModel;

/**
 * An interface for querying/modifying the games stored in the database.
 *
 */
public interface IGameDAO {

	/**
	 * This is the method that will be called to populate the local list of
	 * games.
	 *
	 * @return a list of TransportModels, one for each game
	 */
	public Collection<TransportModel> getGames();

	/**
	 * This is for adding a model into the database.
	 *
	 * @param model
	 *            the game model to be added
	 * @return the id of the game in the database.
	 */
	public int createGame(TransportModel model);

	/**
	 * This is to update the database entry specified by the gameID.
	 *
	 * @param gameId
	 *            the id of the game to update
	 * @param model
	 *            the new model to store.
	 */
	public void updateGame(int gameId, TransportModel model);

}
