package shared.model.facade;

import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ResourceInvoice;

/**
 * This is the general interface for the abstract facade. Then the client facade
 * and the server facade will extend that.
 *
 */
public interface IModelFacade {
	
	/*
	 * Moves server methods
	 */

	/**
	 * Determines if the player is ready to roll
	 * @param player
	 * @return
	 */
	public boolean canRollNumber(PlayerNumber playerIndex);

	/**
	 * Determines if the Robber can be placed on a location
	 * @param player
	 * @param location
	 * @return
	 */
	public boolean canPlaceRobber(PlayerNumber playerIndex, HexLocation location);

	/**
	 * Determines if the player can rob the victim
	 * 
	 * @param player
	 * @param victim
	 * @return
	 */
	public boolean canRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex);

	/**
	 * Determines if the player can finish their turn
	 * @param player
	 * @return
	 */
	public boolean canFinishTurn(PlayerNumber playerIndex);

	/**
	 * Determines if the player can buy a development card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canBuyDevCard(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the player is playing, if they have a playable Year of
	 * Plenty card, and if they have not played another non-Monument development
	 * card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseYearOfPlenty(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the player is playing, if they can use a Year of Plenty
	 * card, and if the bank has one of each of the two resource types
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @return
	 * @throws CatanException
	 */
	public boolean canPlayYearOfPlenty(PlayerNumber playerIndex, ResourceType resource1,
			ResourceType resource2) throws CatanException;

	/**
	 * Determines if the player is playing, if they have a playable RoadBuilding
	 * card, if they have not played another non-Monument development card, and
	 * if they have at least 2 available roads
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseRoadBuilding(PlayerNumber player) throws CatanException;

	/**
	 * Determines if the player is playing, if they have a playable Soldier
	 * card, and if they have not played another non-Monument development card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseSoldier(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the player is playing, if they have a playable Monopoly
	 * card, and if they have not played another non-Monument development card
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonopoly(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the player is playing and if they have a playable Monument
	 * card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonument(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if a player has the resources to build a road
	 * @param playerIndex
	 * @return if the player has the resources to build a road
	 * @throws CatanException
	 */
	public boolean canBuildRoad(PlayerNumber playerIndex, boolean isFree) throws CatanException;

	/**
	 * Determines if the player has the resources to build a settlement
	 * @param playerIndex
	 * @return if the player has the resources to build a settlement
	 * @throws CatanException
	 */
	public boolean canBuildSettlement(PlayerNumber playerIndex, boolean isFree) throws CatanException;

	/**
	 * Determines if the player has the resources to build a city
	 * @param playerIndex
	 * @return if the player has the resources to build a city
	 * @throws CatanException
	 */
	public boolean canBuildCity(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the player can place a road at the desired location
	 * @param playerIndex
	 * @param edge
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceRoad(PlayerNumber playerIndex, EdgeLocation edge, boolean isSetupPhase) throws CatanException;

	/**
	 * Determines if the player can place a settlement at the desired location
	 * @param playerIndex
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceSettlement(PlayerNumber playerIndex, VertexLocation vertex,
			boolean isSetupPhase);

	/**
	 * Determines if the player can place a city at the desired location
	 * @param playerIndex
	 * @param vertex
	 * @param isSetupPhase
	 * @return
	 */
	public boolean canPlaceCity(PlayerNumber playerIndex, VertexLocation vertex);

	/**
	 * Determines if the player can offer a certain trade
	 * @param invoice
	 * @return
	 */
	public boolean canOfferTrade(ResourceInvoice invoice);

	/**
	 * Determines if the player can accept a certain trade
	 * @param invoice
	 * @return
	 */
	public boolean canAcceptTrade(ResourceInvoice invoice);

	/**
	 * Determines if a player has enough of a certain resource to perform a
	 * maritime trade
	 * @param playerIndex
	 * @param giving
	 * @return
	 * @throws CatanException
	 */
	public boolean canMaritimeTrade(PlayerNumber playerIndex, ResourceType giving) throws CatanException;

	/**
	 * Determines if the player needs to discard cards
	 * @param playerIndex
	 * @return
	 */
	public boolean needsToDiscardCards(PlayerNumber playerIndex);

}
