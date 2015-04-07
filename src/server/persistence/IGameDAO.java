package server.persistence;

import java.util.Collection;

import shared.transport.TransportModel;

/*
 * CREATE Table game
 * (
 * gameId int,
 * model blob
 * );
 */

/**
 * This is the interface for accessing the games table or collection.
 * The cortex will call the classes that implement this class to get access the games information.
 *
 */
public interface IGameDAO {
	
	/**
	 * This is the method that will be called to populate the local list of games.
	 * @return the list of created games and the models of the game.
	 */
	public Collection<TransportModel> getGames();
	
	/**
	 * This is for adding a model into the database.
	 * @param model - model to be added
	 * @return the database id of the game.
	 */
	public int createGame(TransportModel model);
	
	/**
	 * This is to update the database entry specified by the gameID.
	 * @param gameId - the Id of the game to update
	 * @param model - the new model to store.
	 */
	public void updateGame(int gameId, TransportModel model);

}
