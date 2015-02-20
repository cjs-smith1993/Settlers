package clientBackend.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import clientBackend.transport.TransportModel;
import clientBackend.transport.TransportPlayer;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;
import shared.definitions.Status;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public class Facade extends Observable {
	private Board board;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerNumber clientPlayer;
	private int version;
	private ArrayList<Observer> observers;

	private boolean isPlaying(PlayerNumber player) {

		if (this.game.getStatus() == Status.PLAYING
				&& this.game.getCurrentPlayer() == player) {
			return true;
		}

		return false;
	}

	public void initializeModel(TransportModel model) throws CatanException {
		this.board = new Board(model.map);
		List<TransportPlayer> players = new ArrayList<TransportPlayer>(Arrays.asList(model.players));
		this.broker = new Broker(model.bank, model.deck, players,
				this.board.getHarborsByPlayer());
		this.game = new Game(players, model.turnTracker);
		this.scoreboard = new Scoreboard(players, model.turnTracker);
		this.postOffice = new PostOffice(model.chat.lines, model.log.lines);
		this.version = model.version;
		
		for (Observer o : observers) {
			o.notify();
		}
	}

	@Override
	public void addObserver(Observer o) {
		this.observers.add(o);
	}
	
	public boolean canDiscardCards(PlayerNumber player) {

		if (this.game.getStatus() == Status.DISCARDING
				&& this.broker.canDiscardCards(player, 8)) {
			return true;
		}

		return false;
	}

	public boolean canRollNumber(PlayerNumber player) {

		if (this.game.getCurrentPlayer() == player
				&& this.game.getStatus() == Status.ROLLING) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player has the resources to build a road
	 *
	 * @param player
	 * @return if the player has the resources to build a road
	 * @throws CatanException
	 */
	public boolean canBuildRoad(PlayerNumber player, boolean isFree) throws CatanException {

		if (this.isPlaying(player)
				&& (isFree || this.broker.canPurchase(player, PropertyType.ROAD))
				&& this.game.hasRoad(player)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player has the resources to build a settlement
	 *
	 * @param player
	 * @return if the player has the resources to build a settlement
	 * @throws CatanException
	 */
	public boolean canBuildSettlement(PlayerNumber player, boolean isFree) throws CatanException {

		if (this.isPlaying(player)
				&& (isFree || this.broker.canPurchase(player, PropertyType.SETTLEMENT))
				&& this.game.hasSettlement(player)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player has the resources to build a city
	 *
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

	public boolean canOfferTrade(ResourceInvoice invoice) {
		if (this.isPlaying(invoice.getSourcePlayer())
				&& this.broker.canOfferTrade(invoice)) {
			return true;
		}

		return false;
	}

	public boolean canAcceptTrade(ResourceInvoice invoice) {
		if (this.broker.canAcceptTrade(invoice)) {
			return true;
		}

		return false;
	}

	public boolean canMaritimeTrade(PlayerNumber player, ResourceType giving) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canMaritimeTrade(player, giving)) {
			return true;
		}

		return false;
	}

	public boolean canFinishTurn(PlayerNumber player) {
		return this.isPlaying(player);
	}

	/*
	 * / public void finishTurn(PlayerNumber player) throws CatanException { //
	 * if (!this.canFinishTurn(player)) { // throw new
	 * CatanException(CatanExceptionType.ILLEGAL_OPERATION, //
	 * "Cannot finish turn for player " + player); // } // //
	 * this.broker.makeDevelopmentCardsPlayable(player); //
	 * this.game.advanceTurn(); // this.game.setLastDiceRoll(-1); //
	 * this.game.setCurrentPlayerHasRolled(false); // // } /
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
	 * @param player
	 * @param resource1
	 * @param resource2
	 * @return
	 * @throws CatanException
	 */
	public boolean canPlayYearOfPlenty(PlayerNumber player, ResourceType resource1,
			ResourceType resource2)
			throws CatanException {
		if (this.isPlaying(player)
				&& this.canUseYearOfPlenty(player)
				&& this.broker.hasNecessaryResourceAmount(PlayerNumber.BANK, resource1, 1)
				&& this.broker.hasNecessaryResourceAmount(PlayerNumber.BANK, resource2, 1)) {
			return true;
		}

		return false;
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
	public boolean canUseRoadBuilder(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.ROAD_BUILD)
				&& !this.game.hasPlayedDevCard(player)
				&& this.game.getNumRoads(player) >= 2) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the player is playing, if they have a playable Soldier
	 * card, and if they have not played another non-Monument development card
	 *
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseSoldier(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.SOLDIER)
				&& !this.game.hasPlayedDevCard(player)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the player is playing, if they have a playable Monopoly
	 * card, and if they have not played another non-Monument development card
	 *
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonopoly(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.MONOPOLY)
				&& !this.game.hasPlayedDevCard(player)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the player is playing and if they have a playable Monument
	 * card
	 *
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonument(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.MONUMENT)) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if the Robber can be placed on a location
	 *
	 * @param player
	 * @param location
	 * @return
	 */
	public boolean canPlaceRobber(PlayerNumber player, HexLocation location) {

		if (this.game.getCurrentPlayer() == player
				&& this.game.getStatus() == Status.ROBBING
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
	public boolean canRobPlayer(PlayerNumber player, PlayerNumber victim) {

		if (this.game.getCurrentPlayer() == player
				&& this.game.getStatus() == Status.ROBBING
				&& this.broker.hasResourceCard(victim)) {
			return true;
		}

		return false;
	}

	/*
	 * Getters and Setters
	 */

	public PlayerNumber getClientPlayer() {
		return this.clientPlayer;
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
}
