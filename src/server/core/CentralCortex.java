package server.core;

import java.util.Collection;
import java.util.Map;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import shared.dataTransportObjects.DTOGame;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.definitions.ServerExceptionType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
import client.serverCommunication.ServerException;

import com.google.gson.JsonObject;

/**
 * This is our HAL9000. It will be responsible for interfacing with our Game and
 * User managers. This will be our server facade so that we can test our
 * connections with a proxy.
 *
 */
public class CentralCortex implements ICortex {

	private static CentralCortex instance;
	private GameManager gameWarden;
	private UserManager HRDepartment;

	private CentralCortex() {
		
		gameWarden = new GameManager();
		HRDepartment = new UserManager();

	}

	public static CentralCortex getInstance() {
		if (instance == null) {
			instance = new CentralCortex();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateUser(UserCertificate userCert) {
		int userId = userCert.getUserId();
		String username = userCert.getName();
		String password = userCert.getPassword();
		return UserManager.getInstance().authenticateUser(userId, username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateGame(GameCertificate gameCert) {
		int gameId = gameCert.getGameId();
		return GameManager.getInstance().authenticateGame(gameId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userLogin(String username, String password) throws CatanException,
			ServerException {
		UserCertificate cert;
		int id = HRDepartment.getUserId(username, password);
		if (id != -1) {
			cert = new UserCertificate(id, username, password);
		} else {
			throw new ServerException(ServerExceptionType.INVALID_OPERATION, "The username and password did not match");
		}
		return cert;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userRegister(String username, String password) throws CatanException,
			ServerException {
		UserCertificate cert = new UserCertificate(HRDepartment.registerUser(username, password),username,password);
		return cert;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DTOGame> gamesList() throws CatanException, ServerException {
		return gameWarden.getGames();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws CatanException, ServerException {
		return gameWarden.createGame(randomTiles, randomNumbers, randomPorts, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameCertificate gamesJoin(int gameId, CatanColor color) throws CatanException,
			ServerException {
		ServerModelFacade facade = gameWarden.getFacadeById(gameId);
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesSave(int gameId, String name) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesLoad(String name) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(int version) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameReset() throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JsonObject> gameCommands() throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameCommands(Collection<JsonObject> commandList) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSendChat(PlayerNumber playerIndex, String content)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRollNumber(PlayerNumber playerIndex, int number)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesFinishTurn(PlayerNumber playerIndex) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuyDevCard(PlayerNumber playerIndex) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesYearOfPlenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRoadBuilding(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonopoly(PlayerNumber playerIndex, ResourceType resource)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonument(PlayerNumber playerIndex) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildCity(PlayerNumber playerIndex, VertexLocation location)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesOfferTrade(ResourceInvoice invoice) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesAcceptTrade(PlayerNumber playerIndex, boolean willAccept)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesDiscardCards(
			PlayerNumber playerIndex,
			Map<ResourceType, Integer> discardedCards) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

}
