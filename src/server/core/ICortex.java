package server.core;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.JsonObject;

import server.cookies.GameCookie;
import server.cookies.UserCookie;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
import client.backend.dataTransportObjects.DTOGame;
import client.serverCommunication.ServerException;

/**
 * The interface the Commands use to manipulate user and game data
 * @author kevinjreece
 */
public interface ICortex {

	/*
	 * User methods
	 */
	
	/**
	 * Logs a player into the server
	 * @param username
	 * @param password
	 * @return
	 */
	public UserCookie userLogin(String username, String password);

	/**
	 * Registers a new player with the server and logs them in
	 * @param username
	 * @param password
	 * @return
	 */
	public UserCookie userRegister(String username, String password);

	/*
	 * Games methods
	 */

	/**
	 * If user is authentic, returns a list of current games on the server
	 * @param user
	 * @return
	 */
	public Collection<DTOGame> gamesList(UserCookie user);

	/**
	 * If user is authentic, creates a new game on the server
	 * @param user
	 * @param randomTiles
	 * @param randomNumbers
	 * @param randomPorts
	 * @param name
	 * @return
	 */
	public DTOGame gamesCreate(UserCookie user, boolean randomTiles, boolean randomNumbers, boolean randomPorts,
			String name);

	/**
	 * If user is authentic, adds the user to the specified game 
	 * @param user
	 * @param gameId
	 * @param color
	 * @return
	 */
	public GameCookie gamesJoin(UserCookie user, int gameId, CatanColor color);

	/**
	 * If user is authentic, saves the appropriate game to the given file
	 * @param user
	 * @param gameId
	 * @param name
	 * @return
	 */
	public boolean gamesSave(UserCookie user, int gameId, String name);

	/**
	 * If user is authentic, loads a previous game from the given file
	 * @param user
	 * @param name
	 * @return
	 */
	public boolean gamesLoad(UserCookie user, String name);

	/*
	 * Game methods
	 */

	/**
	 * If user and game are authentic and the version is not current, 
	 * returns the updated model for the appropriate game
	 * @param user
	 * @param game
	 * @param version
	 * @return the model of the corresponding game
	 * @throws IOException
	 * @throws ServerException
	 */
	public TransportModel gameModel(UserCookie user, GameCookie game, int version) throws IOException, ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer

	/**
	 * If user and game are authentic, returns the updated model for the appropriate game
	 * @param user
	 * @param game
	 * @return the model of the corresponding game
	 * @throws IOException
	 * @throws ServerException
	 */
	public TransportModel gameModel(UserCookie user, GameCookie game) throws IOException, ServerException;
	
	/**
	 * If user and game are authentic, returns the appropriate game to its state right after the Setup Phase
	 * @param user
	 * @param game
	 * @return the updated model of the corresponding game
	 */
	public TransportModel gameReset(UserCookie user, GameCookie game);

	/**
	 * If user and game are authentic, returns a list of every command that has been executed since the game began
	 * @param user
	 * @param game
	 * @return
	 */
	public Collection<JsonObject> gameCommands(UserCookie user, GameCookie game);

	/**
	 * If user and game are authentic, applies the provided list of commands to the appropriate game
	 * @param user
	 * @param game
	 * @param commandList
	 * @return the updated model of the corresponding game
	 */
	public TransportModel gameCommands(UserCookie user, GameCookie game, Collection<JsonObject> commandList);

	/*
	 * Moves methods
	 */

	/**
	 * If user and game are authentic, sends the provided chat in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param content
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesSendChat(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex, 
			String content);

	/**
	 * If user and game are authentic, rolls the dice for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param number
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesRollNumber(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex,
			int number);

	/**
	 * If user and game are authentic, robs the victim for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesRobPlayer(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);

	/**
	 * If user and game are authentic, finishes the player's turn in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesFinishTurn(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex);

	/**
	 * If user and game are authentic, buys a development card for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuyDevCard(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex);

	/**
	 * If user and game are authentic, uses a player's year of plenty development card in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesYear_of_Plenty(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2);

	/**
	 * If user and game are authentic, uses a player's road building development card in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param spot1
	 * @param spot2
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesRoad_Building(
			UserCookie user, 
			GameCookie game, 
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2);

	/**
	 * If user and game are authentic, uses a player's soldier development card in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesSoldier(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);

	/**
	 * If user and game are authentic, uses a player's monopoly development card in the appropriate game
	 * @param user
	 * @param game
	 * @param resource
	 * @param playerIndex
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesMonopoly(
			UserCookie user, 
			GameCookie game,
			ResourceType resource,
			PlayerNumber playerIndex);

	/**
	 * If user and game are authentic, uses a player's monument development card in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesMonument(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex);

	/**
	 * If user and game are authentic, builds a road for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param location
	 * @param free
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuildRoad(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free);

	/**
	 * If user and game are authentic, builds a settlement for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param location
	 * @param free
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuildSettlement(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free);

	/**
	 * If user and game are authentic, builds a city for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param location
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuildCity(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex,
			VertexLocation location);

	/**
	 * If user and game are authentic, offers a trade between players in the appropriate game
	 * @param user
	 * @param game
	 * @param invoice
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesOfferTrade(
			UserCookie user, 
			GameCookie game,
			ResourceInvoice invoice);

	/**
	 * If user and game are authentic, accepts a trade between players in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param willAccept
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesAcceptTrade(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex,
			boolean willAccept);

	/**
	 * If user and game are authentic, performs a maritime trade for the player in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param ratio
	 * @param inputResource
	 * @param outputResource
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesMaritimeTrade(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource);

	/**
	 * If user and game are authentic, discards a player's cards in the appropriate game
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param brick
	 * @param ore
	 * @param sheep
	 * @param wheat
	 * @param wood
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesDiscardCards(
			UserCookie user, 
			GameCookie game,
			PlayerNumber playerIndex, 
			int brick, 
			int ore, 
			int sheep, 
			int wheat, 
			int wood);

}
