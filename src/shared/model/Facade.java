package shared.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import client.backend.dataTransportObjects.DTOGame;
import client.frontend.data.PlayerInfo;
import client.serverCommunication.ServerException;
import client.serverCommunication.ServerInterface;
import shared.definitions.AIType;
import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;
import shared.definitions.CatanState;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.transport.TransportLine;
import shared.transport.TransportModel;
import shared.transport.TransportPlayer;

public class Facade extends Observable {
	private static Facade facadeInstance;
	private ServerInterface server;
	private Board board;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerInfo clientPlayer;
	private ResourceInvoice openOffer;
	private int version = 1;
	private int resourceCardLimit = 7;
	private int winnerServerID = -1;
	private boolean gameReady = false;
	private boolean hasDiscarded = false;
	private boolean roadBuildingPlayed = false;
	private boolean isGameFinished = false;
	private boolean hasFinishedFirstRound = false;
	private boolean hasFinishedSecondRound = false;

	private Facade() {
	}

	public static Facade getInstance() {
		if (facadeInstance == null) {
			facadeInstance = new Facade();
		}

		return facadeInstance;
	}

	public void setProxy(ServerInterface server) {
		this.server = server;
	}
	
	public void forceNotifyOberservers() {
		this.setChanged();
		this.notifyObservers();
	}

	public void initializeModel(TransportModel model) throws CatanException {
		if (model.tradeOffer != null) {
			this.openOffer = new ResourceInvoice(model.tradeOffer);
		}
		else {
			this.openOffer = null;
		}
		
//		System.out.print("\nOld state: " + this.getModelState() + "    New state: " + model.turnTracker.status + "\n");
		this.board = new Board(model.map);

		List<TransportPlayer> players = new ArrayList<TransportPlayer>(Arrays.asList(model.players));
		this.broker = new Broker(model.bank, model.deck, players,
				this.board.getHarborsByPlayer());

		this.game = new Game(players, model.turnTracker);
		this.scoreboard = new Scoreboard(players, model.turnTracker);

		List<TransportLine> chat = new ArrayList<TransportLine>(Arrays.asList(model.chat.lines));
		List<TransportLine> log = new ArrayList<TransportLine>(Arrays.asList(model.log.lines));

		this.postOffice = new PostOffice(chat, log);
		this.version = model.version;
		
		this.finishClientSetup();

		if (this.getModelState() != CatanState.DISCARDING) {
			this.hasDiscarded = false;
		}
		
		if (model.winner != -1) {
			winnerServerID = model.winner;
		}
		
		this.setChanged();
		this.notifyObservers();
		/* Order of Observers to Notify:
		 * 1) Join Game
		 * 2) Player Waiting
		 * 3) Roll
		 * 4) Discard
		 * 5) Resource Bar
		 * 6) Points
		 * 7) Play Dev Card
		 * 8) Turn Tracker
		 * 9) Chat
		 * 10) Game History
		 * 11) Map
		 * 12) Maritime Trade
		 * 13) Domestic Trade
		 */
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}
	/* Order of Observers added:
	 * 1) Domestic Trade
	 * 2) Maritime Trade
	 * 3) Map
	 * 4) Game History
	 * 5) Chat
	 * 6) Turn Tracker
	 * 7) Play Dev Card
	 * 8) Points
	 * 9) Resource Bar
	 * 10) Discard
	 * 11) Roll
	 * 12) Player Waiting
	 * 13) Join Game
	 */

	/**
	 * Determines if the model is changing state
	 *
	 * @param newState
	 * @param queryState
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean hasChangedState(CatanState newState, CatanState queryState) {
		CatanState oldState = this.getModelState();
		return (oldState == queryState && newState != queryState);
	}

	private boolean isPlaying(PlayerNumber player) {
		if (this.game.getState() == CatanState.PLAYING
				&& this.game.getCurrentPlayer() == player) {
			return true;
		}

		return false;
	}

	private boolean inSetup() {
		CatanState state = this.game.getState();
		return state == CatanState.FIRST_ROUND || state == CatanState.SECOND_ROUND;
	}

	/*
	 * User server methods
	 */

	/**
	 * Calls userLogin() on the server
	 *
	 * @param username
	 * @param password
	 * @return
	 * @throws ServerException
	 */
	public boolean login(String username, String password) {
		ModelUser newClient = this.server.userLogin(username, password);
		if (newClient == null) {
			return false;
		}

		this.clientPlayer = new PlayerInfo();
		this.clientPlayer.setId(newClient.getUserId());
		this.clientPlayer.setName(newClient.getName());

		return true;
	}

	/**
	 * Calls userRegister() on the server
	 *
	 * @param username
	 * @param password
	 * @return
	 * @throws ServerException
	 */
	public boolean register(String username, String password) {
		boolean success = this.server.userRegister(username, password);
		if (success) {
			this.login(username, password);
		}
		return success;
	}

	/*
	 * Games server methods
	 */

	/**
	 * Calls gamesList() on the server
	 *
	 * @return
	 * @throws ServerException
	 */
	public Collection<DTOGame> getGamesList() {
		return this.server.gamesList();
	}

	/**
	 * Calls gamesCreate() on the server
	 *
	 * @param randomTiles
	 * @param randomNumbers
	 * @param randomPorts
	 * @param name
	 * @return
	 * @throws ServerException
	 * @throws CatanException
	 */
	public DTOGame createGame(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String gameName) throws CatanException {
		if (gameName != null && !gameName.isEmpty()) {
			DTOGame newGame = this.server.gamesCreate(randomTiles, randomNumbers, randomPorts,
					gameName);
			this.joinGame(newGame.id, CatanColor.BLUE);
			return newGame;
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Name of game cannot be empty");
		}
	}

	/**
	 * Calls gamesJoin() on the server
	 *
	 * @param gameId
	 * @param desiredColor
	 * @return
	 * @throws ServerException
	 */
	public boolean joinGame(int gameId, CatanColor desiredColor) {
		boolean success = this.server.gamesJoin(gameId, desiredColor);
		if (success) {
			this.getModel(false);
		}
		return success;
	}

	/**
	 * Calls gamesSave() on the server
	 *
	 * @param gameId
	 * @param fileName
	 * @return
	 * @throws ServerException
	 */
	public boolean saveGame(int gameId, String fileName) throws CatanException {
		if (fileName != null && !fileName.isEmpty()) {
			return this.server.gamesSave(gameId, fileName);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"File name cannot be empty");
		}
	}

	/**
	 * Calls gamesLoad() on the server
	 *
	 * @param fileName
	 * @return
	 * @throws ServerException
	 * @throws CatanException
	 */
	public boolean loadGame(String fileName) throws ServerException, CatanException {
		if (fileName != null && !fileName.isEmpty()) {
			return this.server.gamesLoad(fileName);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"File name cannot be empty");
		}
	}

	/*
	 * Game server methods
	 */

	/**
	 * Calls gameModel() on the server
	 */
	public void getModel(boolean sendVersion) {
		try {
			if (sendVersion) {
				this.server.gameModel(this.version);
			}
			else {
				this.server.gameModel();
			}
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls gameReset() on the server
	 *
	 * @throws ServerException
	 */
	public void resetGame() {
		this.server.gameReset();
	}

	/**
	 * Calls gameAddAI() on the server
	 *
	 * @param AItype
	 */
	public boolean addAI(AIType AItype) {
		boolean success = this.server.gameAddAI(AItype);
		if (success) {
			this.getModel(true);
		}
		return success;
	}

	/**
	 * Calls gameListAI() on the server
	 *
	 * @return
	 */
	public Collection<AIType> getAITypes() {
		return this.server.gameListAI();
	}

	/*
	 * Moves server methods
	 */

	/**
	 * Calls movesSendChat() on the server
	 *
	 * @param playerIndex
	 * @param content
	 * @return
	 */
	public boolean sendChat(PlayerNumber playerIndex, String content) {
		return this.server.movesSendChat(playerIndex, content);
	}

	/**
	 * Determines if the player can roll the dice for their turn
	 *
	 * @param player
	 * @return
	 */
	public boolean canRollNumber(PlayerNumber playerIndex) {

		if (this.game.getCurrentPlayer() == playerIndex
				&& this.game.getState() == CatanState.ROLLING) {
			return true;
		}

		return false;
	}

	/**
	 * Rolls the dice if the player can roll
	 *
	 * @param player
	 *            the player attempting to roll the dice
	 * @return the result of the dice roll, or -1 if the roll was not allowed
	 */
	public int rollNumber(PlayerNumber playerIndex) throws CatanException {
		if (this.canRollNumber(playerIndex)) {
			int number = this.game.rollDice();
			this.server.movesRollNumber(playerIndex, number);
			return number;
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot roll");
		}
	}

	/**
	 * Determines if the Robber can be placed on a location
	 *
	 * @param player
	 * @param location
	 * @return
	 */
	public boolean canPlaceRobber(PlayerNumber playerNumber, HexLocation location, CatanState state) {

		if (this.game.getCurrentPlayer() == playerNumber
				&& this.game.getState() == state
				&& this.board.canMoveRobber(location)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the player can rob the victim
	 *
	 * @param player
	 * @param victim
	 * @return
	 */
	public boolean canRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex, CatanState state) {

		if (this.game.getCurrentPlayer() == playerIndex
				&& this.game.getState() == state
				&& (this.broker.getResourceCardCount(victimIndex, ResourceType.ALL) > 0)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesRobPlayer() on the server
	 *
	 * @param playerIndex
	 * @param victim
	 * @param newLocation
	 * @return
	 * @throws CatanException
	 */
	public boolean robPlayer(PlayerNumber playerIndex,
			PlayerNumber victim,
			HexLocation newLocation,
			CatanState state) throws CatanException {

		if (this.canPlaceRobber(playerIndex, newLocation, state)
				&& this.canRobPlayer(playerIndex, victim, state)) {
			return this.server.movesRobPlayer(playerIndex, victim, newLocation);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot rob player");
		}
	}

	/**
	 * Determines if the player can finish their turn
	 *
	 * @param player
	 * @return
	 */
	public boolean canFinishTurn(PlayerNumber playerIndex) {
		return this.isPlaying(playerIndex) || this.inSetup();
	}

	/**
	 * Calls movesFinishTurn() on the server
	 *
	 * @return
	 * @throws CatanException
	 */
	public boolean finishTurn(PlayerNumber playerIndex) throws CatanException {

		if (this.canFinishTurn(playerIndex)) {
			return this.server.movesFinishTurn(playerIndex);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot finish turn");
		}
	}

	/**
	 * Determines if the player can buy a development card
	 *
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canBuyDevCard(PlayerNumber playerIndex) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canPurchase(playerIndex, PropertyType.DEVELOPMENT_CARD)
				&& this.broker.hasDevelopmentCard(PlayerNumber.BANK)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesBuyDevCard() on the server
	 *
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean buyDevCard(PlayerNumber playerIndex) throws CatanException {

		if (this.canBuyDevCard(playerIndex)) {
			return this.server.movesBuyDevCard(playerIndex);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"Cannot buy a development card");
		}
	}

	/**
	 * Determines if the player is playing, if they have a playable Year of
	 * Plenty card, and if they have not played another non-Monument development
	 * card
	 *
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseYearOfPlenty(PlayerNumber playerIndex) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canPlayDevelopmentCard(playerIndex, DevCardType.YEAR_OF_PLENTY)
				&& !this.game.hasPlayedDevCard(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the player is playing, if they can use a Year of Plenty
	 * card, and if the bank has one of each of the two resource types
	 *
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @return
	 * @throws CatanException
	 */
	public boolean canPlayYearOfPlenty(PlayerNumber playerIndex, ResourceType resource1,
			ResourceType resource2)
			throws CatanException {
		if (this.isPlaying(playerIndex)
				&& this.canUseYearOfPlenty(playerIndex)
				&& this.broker.hasNecessaryResourceAmount(PlayerNumber.BANK, resource1, 1)
				&& this.broker.hasNecessaryResourceAmount(PlayerNumber.BANK, resource2, 1)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesYear_of_Plenty() on the server
	 *
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @return
	 * @throws CatanException
	 */
	public boolean useYearOfPlenty(PlayerNumber playerIndex, ResourceType resource1,
			ResourceType resource2)
			throws CatanException {
		if (this.canPlayYearOfPlenty(playerIndex, resource1, resource2)) {
			return this.server.movesYear_of_Plenty(playerIndex, resource1, resource2);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot use Year of Plenty");
		}
	}

	/**
	 * Determines if the player is playing, if they have a playable RoadBuilding
	 * card, if they have not played another non-Monument development card, and
	 * if they have at least 2 available roads
	 *
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseRoadBuilding(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.ROAD_BUILD)
				&& !this.game.hasPlayedDevCard(player)
				&& this.game.getNumRoads(player) >= 2) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesRoad_Building() on the server
	 *
	 * @param playerIndex
	 * @param edge1
	 * @param edge2
	 * @return
	 * @throws CatanException
	 */
	public boolean useRoadBuilding(PlayerNumber playerIndex, EdgeLocation edge1, EdgeLocation edge2)
			throws CatanException {

		if (this.canUseRoadBuilding(playerIndex)) {
			this.roadBuildingPlayed = false;
			return this.server.movesRoad_Building(playerIndex, edge1, edge2);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot use road building");
		}
	}

	/**
	 * Determines if the player is playing, if they have a playable Soldier
	 * card, and if they have not played another non-Monument development card
	 *
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseSoldier(PlayerNumber playerIndex) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canPlayDevelopmentCard(playerIndex, DevCardType.SOLDIER)
				&& !this.game.hasPlayedDevCard(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesSoldier() on the server
	 *
	 * @param playerIndex
	 * @param victimIndex
	 * @param newLocation
	 * @return
	 * @throws CatanException
	 */
	public boolean useSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation newLocation) throws CatanException {

		if (this.canUseSoldier(playerIndex)
				&& this.canRobPlayer(playerIndex, victimIndex, CatanState.PLAYING)) {
			return this.server.movesSoldier(playerIndex, victimIndex, newLocation);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot use soldier");
		}
	}

	/**
	 * Determines if the player is playing, if they have a playable Monopoly
	 * card, and if they have not played another non-Monument development card
	 *
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonopoly(PlayerNumber playerIndex) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canPlayDevelopmentCard(playerIndex, DevCardType.MONOPOLY)
				&& !this.game.hasPlayedDevCard(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesMonopoly() on the server
	 *
	 * @param playerIndex
	 * @param resource
	 * @return
	 * @throws CatanException
	 */
	public boolean useMonopoly(PlayerNumber playerIndex, ResourceType resource)
			throws CatanException {

		if (this.canUseMonopoly(playerIndex)) {
			return this.server.movesMonopoly(resource, playerIndex);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot use monopoly");
		}
	}

	/**
	 * Determines if the player is playing and if they have a playable Monument
	 * card
	 *
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonument(PlayerNumber playerIndex) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canPlayDevelopmentCard(playerIndex, DevCardType.MONUMENT)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesMonument() on the server
	 *
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean useMonument(PlayerNumber playerIndex) throws CatanException {

		if (this.canUseMonument(playerIndex)) {
			return this.server.movesMonument(playerIndex);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot use monument");
		}
	}

	/**
	 * Determines if a player has the resources to build a road
	 *
	 * @param playerIndex
	 * @return if the player has the resources to build a road
	 * @throws CatanException
	 */
	public boolean canBuildRoad(PlayerNumber playerIndex, boolean isFree) throws CatanException {

		if ((this.isPlaying(playerIndex) || this.inSetup())
				&& (isFree || this.broker.canPurchase(playerIndex, PropertyType.ROAD))
				&& this.game.hasRoad(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesBuildRoad() on the server
	 *
	 * @param playerIndex
	 * @param location
	 * @param isFree
	 * @return
	 * @throws CatanException
	 */
	public boolean buildRoad(PlayerNumber playerIndex, EdgeLocation location, boolean isFree,
			boolean isSetupPhase) throws CatanException {
		if (this.canBuildRoad(playerIndex, isFree)
				&& this.canPlaceRoad(playerIndex, location, isSetupPhase)) {
			
			boolean success = this.server.movesBuildRoad(playerIndex, location, isFree); 
			
			if (success && this.getModelState() == CatanState.FIRST_ROUND) {
				this.setHasFinishedFirstRound(true);
			}
			else if (success && this.getModelState() == CatanState.SECOND_ROUND) {
				this.setHasFinishedSecondRound(true);
			}
			
			return success;
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot build road");
		}
	}

	/**
	 * Determines if a player has the resources to build a settlement
	 *
	 * @param playerIndex
	 * @return if the player has the resources to build a settlement
	 * @throws CatanException
	 */
	public boolean canBuildSettlement(PlayerNumber playerIndex, boolean isFree)
			throws CatanException {

		if ((this.isPlaying(playerIndex) || this.inSetup())
				&& (isFree || this.broker.canPurchase(playerIndex, PropertyType.SETTLEMENT))
				&& this.game.hasSettlement(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesBuildSettlement() on the server
	 *
	 * @param playerIndex
	 * @param vertex
	 * @param isFree
	 * @param isSetupPhase
	 * @return
	 * @throws CatanException
	 */
	public boolean buildSettlement(PlayerNumber playerIndex, VertexLocation vertex, boolean isFree,
			boolean isSetupPhase) throws CatanException {
		if (this.canBuildSettlement(playerIndex, isFree)
				&& this.canPlaceSettlement(playerIndex, vertex, isSetupPhase)) {
			return this.server.movesBuildSettlement(playerIndex, vertex, isFree);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot build settlement");
		}
	}

	/**
	 * Determines if a player has the resources to build a city
	 *
	 * @param playerIndex
	 * @return if the player has the resources to build a city
	 * @throws CatanException
	 */
	public boolean canBuildCity(PlayerNumber playerIndex) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canPurchase(playerIndex, PropertyType.CITY)
				&& this.game.hasCity(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesBuildCity() on the server
	 *
	 * @param playerIndex
	 * @param vertex
	 * @return
	 * @throws CatanException
	 */
	public boolean buildCity(PlayerNumber playerIndex, VertexLocation vertex) throws CatanException {

		if (this.canBuildCity(playerIndex)
				&& this.canPlaceCity(playerIndex, vertex)) {
			return this.server.movesBuildCity(playerIndex, vertex);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot build city");
		}
	}

	/**
	 * Determines if a player can place a road at the desired location
	 *
	 * @param playerIndex
	 * @param edge
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceRoad(PlayerNumber playerIndex, EdgeLocation edge, boolean isSetupPhase) {

		if ((this.isPlaying(playerIndex) || this.inSetup())
				&& this.board.canPlaceRoad(playerIndex, edge, isSetupPhase)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player can place a settlement at the desired location
	 *
	 * @param playerIndex
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceSettlement(PlayerNumber playerIndex, VertexLocation vertex,
			boolean isSetupPhase) {

		if ((this.isPlaying(playerIndex) || this.inSetup())
				&& this.board.canPlaceSettlement(playerIndex, vertex, isSetupPhase)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player can place a city at the desired location
	 *
	 * @param playerIndex
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceCity(PlayerNumber playerIndex, VertexLocation vertex) {
		boolean isSetupPhase = false;

		if (this.isPlaying(playerIndex)
				&& this.board.canPlaceCity(playerIndex, vertex, isSetupPhase)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player can offer a certain trade
	 *
	 * @param invoice
	 * @return
	 */
	public boolean canOfferTrade(ResourceInvoice invoice) {
		if (this.isPlaying(invoice.getSourcePlayer())
				&& this.broker.canOfferTrade(invoice)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesOfferTrade() on the server
	 *
	 * @param invoice
	 * @return
	 * @throws CatanException
	 */
	public boolean offerTrade(ResourceInvoice invoice) throws CatanException {

		if (this.canOfferTrade(invoice)) {
			return this.server.movesOfferTrade(invoice);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot offer trade");
		}
	}

	/**
	 * Determines if a player can accept a certain trade
	 *
	 * @param invoice
	 * @return
	 */
	public boolean canAcceptTrade(ResourceInvoice invoice) {

		if (this.broker.canAcceptTrade(invoice)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesAcceptTrade() on the server
	 *
	 * @param invoice
	 * @param willAccept
	 * @return
	 * @throws CatanException
	 */
	public boolean acceptTrade(ResourceInvoice invoice, boolean willAccept) throws CatanException {

		if (willAccept && this.canAcceptTrade(invoice)) {
			return this.server.movesAcceptTrade(invoice.getDestinationPlayer(), willAccept);
		}
		else if (!willAccept) {
			return this.server.movesAcceptTrade(invoice.getDestinationPlayer(), willAccept);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot accept trade");
		}
	}

	/**
	 * Determines if a player has enough of a certain resource to perform a
	 * maritime trade
	 *
	 * @param playerIndex
	 * @param giving
	 * @return
	 * @throws CatanException
	 */
	public boolean canMaritimeTrade(PlayerNumber playerIndex, ResourceType giving)
			throws CatanException {

		if (this.isPlaying(playerIndex)
				&& this.broker.canMaritimeTrade(playerIndex, giving)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesMaritimeTrade() on the server
	 *
	 * @param playerIndex
	 * @param ratio
	 * @param inputResource
	 * @param outputResource
	 * @return
	 * @throws CatanException
	 */
	public boolean maritimeTrade(PlayerNumber playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) throws CatanException {

		if (this.canMaritimeTrade(playerIndex, inputResource)) {
			return this.server
					.movesMaritimeTrade(playerIndex, ratio, inputResource, outputResource);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot maritime trade");
		}
	}

	/**
	 * Determines if the player needs to discard cards
	 *
	 * @param playerIndex
	 * @return
	 */
	public boolean needsToDiscardCards(PlayerNumber playerIndex) {

		if (this.game.getState() == CatanState.DISCARDING
				&& (this.broker.getResourceCardCount(playerIndex, ResourceType.ALL) > this.resourceCardLimit)
				&& !this.hasDiscarded) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesDiscardCards() on the server
	 *
	 * @param playerIndex
	 * @param brick
	 * @param ore
	 * @param sheep
	 * @param wheat
	 * @param wood
	 * @return
	 * @throws CatanException
	 */
	public boolean discardCards(PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat,
			int wood)
			throws CatanException {

		if (this.needsToDiscardCards(playerIndex)) {
			this.hasDiscarded = true;
			boolean success = this.server.movesDiscardCards(playerIndex, brick, ore, sheep, wheat,
					wood);
			if (!success) {
				this.hasDiscarded = false;
			}
			return success;
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Cannot discard cards");
		}
	}

	/*
	 * Facade Getters and Setters
	 */

	public PlayerInfo getClientPlayer() {
		return this.clientPlayer;
	}

	public PlayerNumber getClientPlayerIndex() {
		return this.clientPlayer.getPlayerIndex();
	}

	public CatanColor getClientPlayerColor() {
		return this.clientPlayer.getColor();
	}

	public void setClientPlayer(PlayerInfo clientPlayer) {
		this.clientPlayer = clientPlayer;
	}

	public void finishClientSetup() {
		Collection<Player> players = this.game.getPlayers().values();
		for (Player player : players) {
			if (player.getUser().getUserId() == this.clientPlayer.getId()) {
				this.clientPlayer.setPlayerIndex(player.getNumber());
				this.clientPlayer.setColor(player.getColor());
			}
		}
	}

	public boolean isGameReady() {
		return this.gameReady;
	}

	public void setGameReady(boolean ready) {
		this.gameReady = ready;
	}

	public Board getBoard() {
		return this.board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Broker getBroker() {
		return this.broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public PostOffice getPostOffice() {
		return this.postOffice;
	}

	public void setPostOffice(PostOffice postOffice) {
		this.postOffice = postOffice;
	}

	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public int getVersion() {
		return this.version;
	}

	public ResourceInvoice getOpenOffer() {
		return this.openOffer;
	}

	public boolean hasGameFinished() {
		return this.isGameFinished;
	}

	public void setGameFinished(boolean isGameFinished) {
		this.isGameFinished = isGameFinished;
	}

	public boolean roadBuildingPlayed() {
		return this.roadBuildingPlayed;
	}

	public void setRoadBuildingPlayed(boolean played) {
		this.roadBuildingPlayed = played;
	}
	
	public int getWinnerID() {
		return winnerServerID;
	}
	
	public void setWinnerID(int winnerServerID) {
		this.winnerServerID = winnerServerID;
	}

	/*
	 * "Model" Getters and Setters
	 */

	public boolean hasFinishedFirstRound() {
		return hasFinishedFirstRound;
	}

	public void setHasFinishedFirstRound(boolean hasFinishedFirstRound) {
		this.hasFinishedFirstRound = hasFinishedFirstRound;
	}

	public boolean hasFinishedSecondRound() {
		return hasFinishedSecondRound;
	}

	public void setHasFinishedSecondRound(boolean hasFinishedSecondRound) {
		this.hasFinishedSecondRound = hasFinishedSecondRound;
	}

	public List<Message> getMessages() {
		return this.postOffice.getMessages();
	}

	public List<Message> getLog() {
		return this.postOffice.getLog();
	}

	public CatanColor getPlayerColor(PlayerNumber player) {
		return this.game.getPlayers().get(player).getColor();
	}

	public int getResourceCount(PlayerNumber playerIndex, ResourceType resource) {
		return this.broker.getResourceCardCount(playerIndex, resource);
	}

	public int getHoldingCount(PropertyType property) {
		Player curPlayer = this.game.getPlayers().get(this.getClientPlayerIndex());
		switch (property) {
		case ROAD:
			return curPlayer.getNumRoads();
		case SETTLEMENT:
			return curPlayer.getNumSettlements();
		case CITY:
			return curPlayer.getNumCities();
		default:
			return -1;
		}
	}

	public int getPlayerScore(PlayerNumber player) {
		return this.scoreboard.getPoints(player);
	}

	public CatanState getModelState() {
		if (this.game == null) {
			return CatanState.PLAYING;
		}
		return this.game.getState();
	}

	public boolean isClientTurn() {
		return this.getClientPlayer().getPlayerIndex() == this.game.getCurrentPlayer();
	}

	public List<PlayerInfo> getPlayers() {
		Map<PlayerNumber, Player> fullPlayers = this.game.getPlayers();
		List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();

		for (Player eachPlayer : fullPlayers.values()) {
			PlayerInfo info = new PlayerInfo(eachPlayer.getUser().getUserId(),
					eachPlayer.getNumber(),
					eachPlayer.getUser().getName(),
					eachPlayer.getColor());
			playerInfos.add(info);
		}

		return playerInfos;
	}

	public PlayerNumber getPlayerNumberForName(String name) {
		for (PlayerInfo player : this.getPlayers()) {
			if (player.getName().equals(name)) {
				return player.getPlayerIndex();
			}
		}

		return null;
	}

	public PlayerNumber getLongestRoadPlayer() {
		return this.scoreboard.getLongestRoadPlayer();
	}

	public PlayerNumber getLargestArmyPlayer() {
		return this.scoreboard.getLargestArmyPlayer();
	}

	public int getBestMaritimeTradeRatio(PlayerNumber playerIndex, ResourceType type) {
		return this.broker.findBestRatio(playerIndex, type);
	}

	public int getNumberToDiscard(PlayerNumber playerIndex) {
		if (this.needsToDiscardCards(playerIndex)) {
			return this.broker.getNumberToDiscard(playerIndex);
		}
		else {
			return 0;
		}
	}

	/**
	 * Determines whether a player has any remaining development cards.
	 *
	 * @param player
	 * @return
	 */
	public boolean hasDevelopmentCard(PlayerNumber player) {
		return this.broker.hasDevelopmentCard(player);
	}

	public boolean canPlayDevelopmentCard(PlayerNumber player, DevCardType type)
			throws CatanException {
		return this.broker.canPlayDevelopmentCard(player, type);
	}

	public int getDevelopmentCardCount(PlayerNumber player, DevCardType type) throws CatanException {
		return this.broker.getDevelopmentCardCount(player, type);
	}

	public boolean canPurchase(PlayerNumber playerIndex, PropertyType property) {
		try {
			return this.broker.canPurchase(playerIndex, property);
		} catch (CatanException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getWinner() {
		String winnerName = null;

		List<PlayerInfo> players = this.getPlayers();

		if (players != null) {
			PlayerNumber winner = this.scoreboard.getWinner();

			if (winner != PlayerNumber.BANK) {
				winnerName = this.getNameForPlayerNumber(winner);
			}
		}

		return winnerName;
	}

	public String getNameForPlayerNumber(PlayerNumber player) {
		String playerName = null;
		List<PlayerInfo> players = this.getPlayers();

		if (player == PlayerNumber.BANK) {
			return null;
		}

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerIndex() == player) {
				playerName = players.get(i).getName();
			}
		}

		return playerName;
	}

	public int getClientsPlayedSoldiers() throws CatanException {
		return this.broker.getNumberOfPlayedSoldiers(this.getClientPlayerIndex());
	}
	
	public boolean hasPlayedDevCard(PlayerNumber playerIndex) {
		return this.game.hasPlayedDevCard(playerIndex);
	}
}