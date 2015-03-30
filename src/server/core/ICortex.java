package server.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.ICommand;
import shared.dataTransportObjects.DTOGame;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
import client.serverCommunication.ServerException;

/**
 * The interface the Commands use to manipulate user and game data
 *
 * @author kevinjreece
 */
public interface ICortex {

	/**
	 * Authentication methods
	 */

	/**
	 * Authenticates a user certificate
	 *
	 * @param userCert
	 *            a user certificate
	 * @return true if the user certificate is valid
	 */
	public boolean authenticateUser(UserCertificate userCert);

	/**
	 * Authenticates a game certificate
	 *
	 * @param userCert
	 *            a game certificate
	 * @return true if the game certificate is valid
	 */
	public boolean authenticateGame(GameCertificate gameCert);

	/*
	 * User methods
	 */

	/**
	 * Logs a player into the server
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	public UserCertificate userLogin(String username, String password) throws CatanException,
			ServerException;

	/**
	 * Registers a new player with the server and logs them in
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	public UserCertificate userRegister(String username, String password) throws CatanException,
			ServerException;

	/*
	 * Games methods
	 */

	/**
	 * If user is authentic, returns a list of current games on the server
	 *
	 * @param user
	 * @return
	 */
	public Collection<DTOGame> gamesList() throws CatanException, ServerException;

	/**
	 * If user is authentic, creates a new game on the server
	 *
	 * @param user
	 * @param randomTiles
	 * @param randomNumbers
	 * @param randomPorts
	 * @param name
	 * @return
	 */
	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws CatanException, ServerException;

	/**
	 * If user is authentic, adds the user to the specified game
	 *
	 * @param gameId
	 * @param color
	 * @param playerId
	 *            TODO
	 * @param user
	 *
	 * @return
	 */
	public GameCertificate gamesJoin(int gameId, CatanColor color, int playerId)
			throws CatanException, ServerException;

	/**
	 * If user is authentic, saves the appropriate game to the given file
	 *
	 * @param user
	 * @param gameId
	 * @param name
	 * @return
	 * @throws FileNotFoundException 
	 */
	public boolean gamesSave(int gameId, String name) throws CatanException, ServerException, FileNotFoundException;

	/**
	 * If user is authentic, loads a previous game from the given file
	 *
	 * @param user
	 * @param name
	 * @return
	 */
	public boolean gamesLoad(String name) throws CatanException, ServerException;

	/*
	 * Game methods
	 */

	/**
	 * If user and game are authentic and the version is not current, returns
	 * the updated model for the appropriate game
	 *
	 * @param version
	 * @param gameId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the model of the corresponding game
	 * @throws IOException
	 * @throws ServerException
	 */
	public TransportModel gameModel(int version, int gameId) throws CatanException, ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer

	/**
	 * If user and game are authentic, returns the appropriate game to its state
	 * right after the Setup Phase
	 *
	 * @param gameId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel gameReset(int gameId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, returns a list of every command that has
	 * been executed since the game began
	 *
	 * @param gameId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return
	 */
	public Collection<ICommand> gameCommands(int gameId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, applies the provided list of commands to
	 * the appropriate game
	 *
	 * @param commandList
	 * @param gameId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel gameCommands(Collection<ICommand> commandList, int gameId)
			throws CatanException, ServerException;

	/*
	 * Moves methods
	 */

	/**
	 * If user and game are authentic, sends the provided chat in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param content
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesSendChat(
			PlayerNumber playerIndex,
			String content,
			int gameId,
			int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, rolls the dice for the player in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param number
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesRollNumber(
			PlayerNumber playerIndex,
			int number,
			int gameId,
			int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, robs the victim for the player in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, finishes the player's turn in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesFinishTurn(PlayerNumber playerIndex, int gameId, int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, buys a development card for the player in
	 * the appropriate game
	 *
	 * @param playerIndex
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuyDevCard(PlayerNumber playerIndex, int gameId, int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, uses a player's year of plenty
	 * development card in the appropriate game
	 *
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesYearOfPlenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, uses a player's road building development
	 * card in the appropriate game
	 *
	 * @param playerIndex
	 * @param spot1
	 * @param spot2
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesRoadBuilding(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, uses a player's soldier development card
	 * in the appropriate game
	 *
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, uses a player's monopoly development card
	 * in the appropriate game
	 *
	 * @param playerIndex
	 * @param resource
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesMonopoly(
			PlayerNumber playerIndex,
			ResourceType resource,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, uses a player's monument development card
	 * in the appropriate game
	 *
	 * @param playerIndex
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesMonument(PlayerNumber playerIndex, int gameId, int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, builds a road for the player in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param location
	 * @param free
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, builds a settlement for the player in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param location
	 * @param free
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, builds a city for the player in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param location
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesBuildCity(
			PlayerNumber playerIndex,
			VertexLocation location,
			int gameId,
			int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, offers a trade between players in the
	 * appropriate game
	 *
	 * @param invoice
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesOfferTrade(ResourceInvoice invoice, int gameId, int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, accepts a trade between players in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param willAccept
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesAcceptTrade(
			PlayerNumber playerIndex,
			boolean willAccept,
			int gameId,
			int userId)
			throws CatanException, ServerException;

	/**
	 * If user and game are authentic, performs a maritime trade for the player
	 * in the appropriate game
	 *
	 * @param playerIndex
	 * @param ratio
	 * @param inputResource
	 * @param outputResource
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource,
			int gameId,
			int userId) throws CatanException, ServerException;

	/**
	 * If user and game are authentic, discards a player's cards in the
	 * appropriate game
	 *
	 * @param playerIndex
	 * @param gameId
	 *            TODO
	 * @param userId
	 *            TODO
	 * @param user
	 * @param game
	 * @param brick
	 * @param ore
	 * @param sheep
	 * @param wheat
	 * @param wood
	 *
	 * @return the updated model of the corresponding game
	 */
	public TransportModel movesDiscardCards(
			PlayerNumber playerIndex,
			Map<ResourceType, Integer> discardedCards,
			int gameId,
			int userId) throws CatanException, ServerException;

}
