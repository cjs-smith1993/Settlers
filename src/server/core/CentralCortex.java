package server.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.ICommand;
import server.commands.moves.AbstractMovesCommand;
import server.persistence.IPersistenceProvider;
import server.persistence.PersistenceProviderFactory;
import shared.dataTransportObjects.DTOGame;
import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ModelUser;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
import client.serverCommunication.ServerException;

/**
 * This is our HAL9000. It will be responsible for interfacing with our Game and
 * User managers. This will be our server facade so that we can test our
 * connections with a proxy.
 *
 */
public class CentralCortex implements ICortex {

	private static CentralCortex instance;
	private GameManager gameManager;
	private UserManager userManager;
	private CommandManager commandManager;

	private CentralCortex() throws IOException {
		this.gameManager = GameManager.getInstance();
		this.userManager = UserManager.getInstance();
		this.commandManager = CommandManager.getInstance();
	}

	public static CentralCortex getInstance() {
		if (instance == null) {
			try {
				instance = new CentralCortex();
			} catch (IOException err) {
				err.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * This method is used to inject the {@link PersistenceProviderFactory}. 
	 * This factorythat will be used to interact with the database.
	 * @param provider
	 */
	public void setPersistenceProviderFactory(IPersistenceProvider persistenceProvider) {
		// TODO: Implement in the afterlife.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateUser(UserCertificate userCert) {
		int userId = userCert.getUserId();
		String username = userCert.getName();
		String password = userCert.getPassword();
		return this.userManager.authenticateUser(userId, username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateGame(GameCertificate gameCert) {
		int gameId = gameCert.getGameId();
		return this.gameManager.authenticateGame(gameId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userLogin(String username, String password) throws CatanException,
			ServerException {
		UserCertificate cert;
		int id = this.userManager.getUserId(username, password);
		if (id != -1) {
			cert = new UserCertificate(id, username, password);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"The username and password did not match");
		}
		return cert;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userRegister(String username, String password) throws CatanException,
			ServerException {
		UserCertificate cert = new UserCertificate(this.userManager.registerUser(username,
				password), username, password);
		return cert;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DTOGame> gamesList() throws CatanException, ServerException {
		return this.gameManager.getGames();
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
		return this.gameManager.createGame(randomTiles, randomNumbers, randomPorts, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameCertificate gamesJoin(int gameId, CatanColor color, int playerId)
			throws CatanException,
			ServerException {
		//change game id if needed
		ModelUser user = this.userManager.getModelUser(playerId);

		return this.gameManager.joinGame(gameId, color, user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesSave(int gameId, String name) throws CatanException, ServerException, FileNotFoundException {
		return this.gameManager.saveGameToFile(gameId, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesLoad(String filename) throws CatanException, ServerException {
		return this.gameManager.createGameFromFile(filename);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(int version, int gameId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return version == -1 ? facade.getModel() : facade.getModel(version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameReset(int gameId) throws CatanException, ServerException {
		if (gameId == 0) {
			this.gameManager.createGameFromFile("save/games/DefaultGame.txt");
		}
		else {
			return this.gameManager.resetGame(gameId);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<AbstractMovesCommand> gameCommands(int gameId) throws CatanException, ServerException {
		return this.commandManager.getAllCommands(gameId);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game they are in
	@Override
	public TransportModel movesSendChat(
			PlayerNumber playerIndex,
			String content,
			int gameId,
			int userId)
			throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);

		return facade.sendChat(playerIndex, content);
	}

	/**
	 * {@inheritDoc}
	 */
	//the game the number is rolled in
	//and isn't our server rolling for people?
	@Override
	public TransportModel movesRollNumber(
			PlayerNumber playerIndex,
			int number,
			int gameId,
			int userId)
			throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.rollNumber(playerIndex, number);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game do we call this on
	@Override
	public TransportModel movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location, int gameId, int userId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.robPlayer(playerIndex, victimIndex, location);//might need to not have the state
	}

	/**
	 * {@inheritDoc}
	 */
	//which game is this player in
	@Override
	public TransportModel movesFinishTurn(PlayerNumber playerIndex, int gameId, int userId)
			throws CatanException,
			ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);//set a flag to know if the commands are being loaded
		return facade.finishTurn(playerIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game ??
	@Override
	public TransportModel movesBuyDevCard(PlayerNumber playerIndex, int gameId, int userId)
			throws CatanException,
			ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.buyDevCard(playerIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game
	@Override
	public TransportModel movesYearOfPlenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2, int gameId, int userId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.useYearOfPlenty(playerIndex, resource1, resource2);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game
	@Override
	public TransportModel movesRoadBuilding(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2, int gameId, int userId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.useRoadBuilding(playerIndex, spot1, spot2);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game
	@Override
	public TransportModel movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location, int gameId, int userId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.useSoldier(playerIndex, victimIndex, location);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game
	@Override
	public TransportModel movesMonopoly(
			PlayerNumber playerIndex,
			ResourceType resource,
			int gameId,
			int userId)
			throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.useMonopoly(playerIndex, resource);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game
	@Override
	public TransportModel movesMonument(PlayerNumber playerIndex, int gameId, int userId)
			throws CatanException,
			ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.useMonument(playerIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game
	@Override
	public TransportModel movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free, int gameId, int userId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.buildRoad(playerIndex, location, free);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game??
	@Override
	public TransportModel movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free, int gameId, int userId) throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.buildSettlement(playerIndex, location, free);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game??
	@Override
	public TransportModel movesBuildCity(
			PlayerNumber playerIndex,
			VertexLocation location,
			int gameId,
			int userId)
			throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.buildCity(playerIndex, location);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game??
	@Override
	public TransportModel movesOfferTrade(ResourceInvoice invoice, int gameId, int userId)
			throws CatanException,
			ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.offerTrade(invoice);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game??
	@Override
	public TransportModel movesAcceptTrade(
			PlayerNumber playerIndex,
			boolean willAccept,
			int gameId,
			int userId)
			throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.acceptTrade(userId, willAccept);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game??
	@Override
	public TransportModel movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource, int gameId, int userId) throws CatanException,
			ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		return facade.maritimeTrade(playerIndex, ratio, inputResource, outputResource);
	}

	/**
	 * {@inheritDoc}
	 */
	//which game??
	@Override
	public TransportModel movesDiscardCards(
			PlayerNumber playerIndex,
			Map<ResourceType, Integer> discardedCards, int gameId, int userId)
			throws CatanException, ServerException {
		ServerModelFacade facade = this.gameManager.getFacadeById(gameId);
		int brick = 0;
		int ore = 0;
		int sheep = 0;
		int wheat = 0;
		int wood = 0;
		for (ResourceType type : ResourceType.values()) {
			switch (type) {
			case BRICK:
				brick = discardedCards.get(type);
				break;
			case ORE:
				ore = discardedCards.get(type);
				break;
			case SHEEP:
				sheep = discardedCards.get(type);
				break;
			case WHEAT:
				wheat = discardedCards.get(type);
				break;
			case WOOD:
				wood = discardedCards.get(type);
				break;
			default:
				break;
			}
		}
		return facade.discardCards(playerIndex, brick, ore, sheep, wheat, wood);
	}

	public void saveCommand(int gameId, AbstractMovesCommand command) {
		this.commandManager.addCommand(gameId, command);
	}
}
