package client.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import client.frontend.data.PlayerInfo;
import client.serverCommunication.ServerException;
import client.serverCommunication.ServerInterface;
import shared.dataTransportObjects.DTOGame;
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
import shared.model.Board;
import shared.model.Broker;
import shared.model.CatanException;
import shared.model.Game;
import shared.model.Message;
import shared.model.ModelUser;
import shared.model.Player;
import shared.model.PostOffice;
import shared.model.ResourceInvoice;
import shared.model.Scoreboard;
import shared.model.facade.AbstractModelFacade;
import shared.transport.TransportLine;
import shared.transport.TransportModel;
import shared.transport.TransportPlayer;

public class ClientModelFacade extends AbstractModelFacade {
	private static ClientModelFacade facadeInstance;
	private ServerInterface server;
	private PlayerInfo clientPlayer;
	private ResourceInvoice openOffer;
	private boolean gameReady = false;
	private boolean hasDiscarded = false;
	private boolean roadBuildingPlayed = false;
	private boolean isGameFinished = false;
	private boolean hasFinishedFirstRound = false;
	private boolean hasFinishedSecondRound = false;

	private ClientModelFacade() {
	}

	public static ClientModelFacade getInstance() {
		if (facadeInstance == null) {
			facadeInstance = new ClientModelFacade();
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
	public boolean canPlaceRobber(PlayerNumber playerIndex, HexLocation location, CatanState state) {

		if (super.canPlaceRobber(playerIndex, location)
				&& this.game.getState() == state) {
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

		if (super.canRobPlayer(playerIndex, victimIndex)
				&& this.game.getState() == state) {
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
	@Override
	public boolean needsToDiscardCards(PlayerNumber playerIndex) {

		if (super.needsToDiscardCards(playerIndex)
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

	public int getClientsPlayedSoldiers() throws CatanException {
		return this.broker.getNumberOfPlayedSoldiers(this.getClientPlayerIndex());
	}
	
	public boolean hasPlayedDevCard(PlayerNumber playerIndex) {
		return this.game.hasPlayedDevCard(playerIndex);
	}
}
