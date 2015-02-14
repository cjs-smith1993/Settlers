package clientBackend.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import clientBackend.transport.TransportModel;
import clientBackend.transport.TransportPlayer;
import shared.definitions.CatanExceptionType;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;
import shared.definitions.Status;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public class Facade {
	private Board board;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerNumber clientPlayer;
	private int version;

	private final int ROBBER_ROLL = 7;
	
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
	public boolean canBuildRoad(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available road?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPurchase(player, PropertyType.ROAD)
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
	public boolean canBuildSettlement(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available settlement?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPurchase(player, PropertyType.SETTLEMENT)
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
	 * @param player
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceSettlement(PlayerNumber player, VertexLocation vertex, boolean isSetupPhase) {
		
		if (this.isPlaying(player)
				&& this.board.canPlaceDwelling(player, vertex, isSetupPhase, PropertyType.SETTLEMENT)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if a player can place a city at the desired location
	 * @param player
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceCity(PlayerNumber player, VertexLocation vertex) {
		boolean isSetupPhase = false;
		
		if (this.isPlaying(player)
				&& this.board.canPlaceDwelling(player, vertex, isSetupPhase, PropertyType.CITY)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canOfferTrade(PlayerNumber player, ResourceInvoice invoice) {

		if (this.isPlaying(player)
				&& this.broker.hasResourceCard(player)) {
			return true;
		}

		return false;
	}

	public boolean canAcceptTrade(PlayerNumber player) {
		// Does the player have any resource cards?

		if (this.broker.hasResourceCard(player)) {
			return true;
		}

		return false;
	}

	public boolean canMaritimeTrade(PlayerNumber player, int ratio, ResourceType giving, ResourceType getting) throws CatanException {
		
		if (this.isPlaying(player)
				&& this.broker.hasHarbor(player)) {
			return true;
		}

		return false;
	}

	public boolean canFinishTurn(PlayerNumber player) {
		return isPlaying(player);
	}

	public void finishTurn(PlayerNumber player) throws CatanException {
		if (!this.canFinishTurn(player)) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot finish turn for player " + player);
		}

		this.broker.makeDevelopmentCardsPlayable(player);
		this.game.advanceTurn();
		this.game.setLastDiceRoll(-1);
		this.game.setCurrentPlayerHasRolled(false);

	}

	public boolean canBuyDevCard(PlayerNumber player) throws CatanException {

		if (this.isPlaying(player)
				&& this.broker.canPurchase(player, PropertyType.DEVELOPMENT_CARD)
				&& this.broker.hasDevelopmentCard(player)) {
			return true;
		}

		return false;
	}

	public boolean canUseYearOfPlenty(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Year of Plenty
		// developement card?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.YEAR_OF_PLENTY)) {
			return true;
		}

		return false;
	}

	public boolean canUseRoadBuilder(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Road Builder
		// developement card?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.ROAD_BUILD)) {
			return true;
		}

		return false;
	}

	public boolean canUseSoldier(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have an available Soldier
		// developement card?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.SOLDIER)) {
			return true;
		}

		return false;
	}

	public boolean canUseMonopoly(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Monopoly
		// development card?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.MONOPOLY)) {
			return true;
		}

		return false;
	}

	public boolean canUseMonument(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Monument
		// development card?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.broker.canPlayDevelopmentCard(player, DevCardType.MONUMENT)) {
			return true;
		}

		return false;
	}

	public boolean canPlaceRobber(PlayerNumber player) {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Was a 7 rolled?

		if (this.game.getCurrentPlayerHasRolled()
				&& this.game.getCurrentPlayer() == player
				&& this.game.getLastDiceRoll() == this.ROBBER_ROLL) {
			return true;
		}

		return false;
	}

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
