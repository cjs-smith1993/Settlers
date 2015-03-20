package shared.model.facade;

import shared.definitions.CatanState;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Board;
import shared.model.Broker;
import shared.model.CatanException;
import shared.model.Game;
import shared.model.PostOffice;
import shared.model.ResourceInvoice;
import shared.model.Scoreboard;


/**
 * Represents an abstract notion of a facade for interacting with model objects
 * and data.
 *
 */
public abstract class AbstractModelFacade implements IModelFacade {
	
	private Board board;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private ResourceInvoice openOffer;
	private int version = 1;
	private int winnerServerID = -1;
	private int resourceCardLimit = 7;
	private boolean hasDiscarded = false;
	
	
	
	private boolean inSetup() {
		CatanState state = this.game.getState();
		return state == CatanState.FIRST_ROUND || state == CatanState.SECOND_ROUND;
	}
	
	private boolean isPlaying(PlayerNumber player) {
		if (this.game.getState() == CatanState.PLAYING
				&& this.game.getCurrentPlayer() == player) {
			return true;
		}

		return false;
	}
	/*
	 * Moves server methods
	 */

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
	 * Determines if the player can finish their turn
	 *
	 * @param player
	 * @return
	 */
	public boolean canFinishTurn(PlayerNumber playerIndex) {
		return this.isPlaying(playerIndex) || this.inSetup();
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

}
