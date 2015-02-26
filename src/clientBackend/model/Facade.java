package clientBackend.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import client.data.PlayerInfo;
import clientBackend.dataTransportObjects.DTOGame;
import clientBackend.transport.TransportLine;
import clientBackend.transport.TransportModel;
import clientBackend.transport.TransportPlayer;
import serverCommunication.ServerException;
import serverCommunication.ServerInterface;
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

public class Facade extends Observable {
	private static Facade facadeInstance;
	private ServerInterface server;
	private Board board;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerNumber clientPlayer;
	private String clientName;
	private int version = 1;
	private int resourceCardLimit = 7;

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

	public void initializeModel(TransportModel model) throws CatanException {
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
		String someValue = "howdy";

		this.setClientPlayer(this.clientName);

		this.setChanged();
		this.notifyObservers(someValue);
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	private boolean isPlaying(PlayerNumber player) {
		if (this.game.getState() == CatanState.PLAYING
				&& this.game.getCurrentPlayer() == player) {
			return true;
		}

		return false;
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
		return this.server.userLogin(username, password);
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
		return this.server.userRegister(username, password);
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
	public DTOGame createGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts,
			String gameName)
			throws CatanException {
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
			this.getModel();
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
	public void getModel() {
		try {
			this.server.gameModel(this.version);
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
			this.getModel();
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
	public boolean canPlaceRobber(PlayerNumber playerNumber, HexLocation location) {

		if (this.game.getCurrentPlayer() == playerNumber
				&& this.game.getState() == CatanState.ROBBING
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
	public boolean canRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex) {

		if (this.game.getCurrentPlayer() == playerIndex
				&& this.game.getState() == CatanState.ROBBING
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
	public boolean robPlayer(PlayerNumber playerIndex, PlayerNumber victim, HexLocation newLocation)
			throws CatanException {

		if (this.canPlaceRobber(playerIndex, newLocation) && this.canRobPlayer(playerIndex, victim)) {
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
		return this.isPlaying(playerIndex);
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
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canBuyDevCard(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPurchase(player, PropertyType.DEVELOPMENT_CARD)
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
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseYearOfPlenty(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.YEAR_OF_PLENTY)
				&& !this.game.hasPlayedDevCard(player)) {
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

		if (this.canUseRoadBuilding(playerIndex)
				&& this.canPlaceRoad(playerIndex, edge1, false)
				&& this.canPlaceRoad(playerIndex, edge2, false)) {
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
				&& this.canRobPlayer(playerIndex, victimIndex)) {
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

		if (this.isPlaying(playerIndex)
				&& (isFree || this.broker.canPurchase(playerIndex, PropertyType.ROAD))
				&& this.game.hasRoad(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesBuildRoad() on the server
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
			return this.server.movesBuildRoad(playerIndex, location, isFree);
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
	public boolean canBuildSettlement(PlayerNumber playerIndex, boolean isFree) throws CatanException {

		if (this.isPlaying(playerIndex)
				&& (isFree || this.broker.canPurchase(playerIndex, PropertyType.SETTLEMENT))
				&& this.game.hasSettlement(playerIndex)) {
			return true;
		}

		return false;
	}

	/**
	 * Calls movesBuildSettlement() on the server
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
	 * @param player
	 * @return if the player has the resources to build a city
	 * @throws CatanException
	 */
	public boolean canBuildCity(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPurchase(player, PropertyType.CITY)
				&& this.game.hasCity(player)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player can place a road at the desired location
	 *
	 * @param player
	 * @param edge
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceRoad(PlayerNumber player, EdgeLocation edge, boolean isSetupPhase) {

		if (this.isPlaying(player)
				&& this.board.canPlaceRoad(player, edge, isSetupPhase)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player can place a settlement at the desired location
	 *
	 * @param player
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceSettlement(PlayerNumber player, VertexLocation vertex,
			boolean isSetupPhase) {

		if (this.isPlaying(player)
				&& this.board.canPlaceSettlement(player, vertex, isSetupPhase)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player can place a city at the desired location
	 *
	 * @param player
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceCity(PlayerNumber player, VertexLocation vertex) {
		boolean isSetupPhase = false;

		if (this.isPlaying(player)
				&& this.board.canPlaceCity(player, vertex, isSetupPhase)) {
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
	 * Determines if a player has enough of a certain resource to perform a
	 * maritime trade
	 *
	 * @param player
	 * @param giving
	 * @return
	 * @throws CatanException
	 */
	public boolean canMaritimeTrade(PlayerNumber player, ResourceType giving) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canMaritimeTrade(player, giving)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the player needs to discard cards
	 *
	 * @param player
	 * @return
	 */
	public boolean needsToDiscardCards(PlayerNumber player) {

		if (this.game.getState() == CatanState.DISCARDING
				&& (this.broker.getResourceCardCount(player, ResourceType.ALL) > this.resourceCardLimit)) {
			return true;
		}

		return false;
	}

	public boolean discardCards() {
		return false;
	}

	/*
	 * Facade Getters and Setters
	 */

	public PlayerNumber getClientPlayer() {
		return this.clientPlayer;
	}

	public void setClientPlayer(String name) {
		for (Player player : this.game.getPlayers().values()) {
			if (player.getUser().getName().equals(name)) {
				this.clientPlayer = player.getNumber();
			}
		}
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String name) {
		this.clientName = name;
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

	/*
	 * "Model" Getters and Setters
	 */

	public List<Message> getMessages() {
		return this.postOffice.getMessages();
	}

	public List<Message> getLog() {
		return this.postOffice.getLog();
	}

	public CatanColor getPlayerColor(PlayerNumber player) {
		return this.game.getPlayers().get(player).getColor();
	}

	public int getResourceCount(ResourceType resource) {
		return this.broker.getResourceCardCount(this.clientPlayer, resource);
	}

	public int getHoldingCount(PropertyType property) {
		switch (property) {
		case ROAD:
			return this.game.getPlayers().get(this.clientPlayer).getNumRoads();
		case SETTLEMENT:
			return this.game.getPlayers().get(this.clientPlayer).getNumSettlements();
		case CITY:
			return this.game.getPlayers().get(this.clientPlayer).getNumCities();
		default:
			return -1;
		}
	}

	public int getPlayerScore(PlayerNumber player) {
		return this.scoreboard.getPoints(player);
	}

	public CatanState getModelState() {
		return this.game.getState();
	}

	public boolean isClientTurn() {
		return this.getClientPlayer() == this.game.getCurrentPlayer();
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

	public PlayerInfo getClientPlayerInfo() {
		Player client = this.game.getPlayers().get(this.clientPlayer);
		return new PlayerInfo(client.getUser().getUserId(), client.getNumber(), client.getUser().getName(), client.getColor());
	}
	
	public PlayerNumber getLongestRoadPlayer() {
		return this.scoreboard.getLongestRoadPlayer();
	}

	public PlayerNumber getLargestArmyPlayer() {
		return this.scoreboard.getLargestArmyPlayer();
	}
}
