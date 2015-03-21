package shared.model.facade;

import java.util.Collection;

import shared.dataTransportObjects.DTOGame;
import shared.definitions.AIType;
import shared.definitions.CatanColor;
import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import client.serverCommunication.ServerException;

/**
 * This is the general interface for the abstract facade. Then the client facade
 * and the server facade will extend that.
 *
 */
public interface IModelFacade {
	
	/*
	 * Game server methods
	 */

	/**
	 * Gets the updated model from the server
	 */
	public void getModel(boolean sendVersion);

	/*
	 * Moves server methods
	 */

	/**
	 * Sends a chat for the player
	 * @param playerIndex
	 * @param content
	 * @return
	 */
	public boolean sendChat(PlayerNumber playerIndex, String content) throws CatanException;

	/**
	 * Determines if the player is ready to roll
	 * @param player
	 * @return
	 */
	public boolean canRollNumber(PlayerNumber playerIndex);

	/**
	 * Rolls the dice for the player
	 * @param player
	 *            the player attempting to roll the dice
	 * @return the result of the dice roll, or -1 if the roll was not allowed
	 */
	public int rollNumber(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the Robber can be placed on a location
	 * @param player
	 * @param location
	 * @return
	 */
	public boolean canPlaceRobber(PlayerNumber playerNumber, HexLocation location, CatanState state);

	/**
	 * Determines if the player can rob the victim
	 * 
	 * @param player
	 * @param victim
	 * @return
	 */
	public boolean canRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex, CatanState state);

	/**
	 * Robs the victim for the player
	 * @param playerIndex
	 * @param victim
	 * @param newLocation
	 * @return
	 * @throws CatanException
	 */
	public boolean robPlayer(PlayerNumber playerIndex,
			PlayerNumber victim,
			HexLocation newLocation,
			CatanState state) throws CatanException;

	/**
	 * Determines if the player can finish their turn
	 * @param player
	 * @return
	 */
	public boolean canFinishTurn(PlayerNumber playerIndex);

	/**
	 * Finishes the player's turn
	 * @return
	 * @throws CatanException
	 */
	public boolean finishTurn(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if the player can buy a development card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canBuyDevCard(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Buys a development card for the player
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean buyDevCard(PlayerNumber playerIndex) throws CatanException;

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
	 * Uses a player's year of plenty development card
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @return
	 * @throws CatanException
	 */
	public boolean useYearOfPlenty(PlayerNumber playerIndex, ResourceType resource1,
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
	 * Uses a player's road building development card
	 * @param playerIndex
	 * @param edge1
	 * @param edge2
	 * @return
	 * @throws CatanException
	 */
	public boolean useRoadBuilding(PlayerNumber playerIndex, EdgeLocation edge1, EdgeLocation edge2) throws CatanException;

	/**
	 * Determines if the player is playing, if they have a playable Soldier
	 * card, and if they have not played another non-Monument development card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseSoldier(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Uses a player's soldier development card
	 * @param playerIndex
	 * @param victimIndex
	 * @param newLocation
	 * @return
	 * @throws CatanException
	 */
	public boolean useSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation newLocation) throws CatanException;

	/**
	 * Determines if the player is playing, if they have a playable Monopoly
	 * card, and if they have not played another non-Monument development card
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonopoly(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Uses a player's monopoly development card
	 * @param playerIndex
	 * @param resource
	 * @return
	 * @throws CatanException
	 */
	public boolean useMonopoly(PlayerNumber playerIndex, ResourceType resource) throws CatanException;

	/**
	 * Determines if the player is playing and if they have a playable Monument
	 * card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean canUseMonument(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Uses a player's monument development card
	 * @param playerIndex
	 * @return
	 * @throws CatanException
	 */
	public boolean useMonument(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Determines if a player has the resources to build a road
	 * @param playerIndex
	 * @return if the player has the resources to build a road
	 * @throws CatanException
	 */
	public boolean canBuildRoad(PlayerNumber playerIndex, boolean isFree) throws CatanException;

	/**
	 * Builds a road for the player
	 * @param playerIndex
	 * @param location
	 * @param isFree
	 * @return
	 * @throws CatanException
	 */
	public boolean buildRoad(PlayerNumber playerIndex, EdgeLocation location, boolean isFree,
			boolean isSetupPhase) throws CatanException;

	/**
	 * Determines if the player has the resources to build a settlement
	 * @param playerIndex
	 * @return if the player has the resources to build a settlement
	 * @throws CatanException
	 */
	public boolean canBuildSettlement(PlayerNumber playerIndex, boolean isFree) throws CatanException;

	/**
	 * Builds a settlement for the player
	 * @param playerIndex
	 * @param vertex
	 * @param isFree
	 * @param isSetupPhase
	 * @return
	 * @throws CatanException
	 */
	public boolean buildSettlement(PlayerNumber playerIndex, VertexLocation vertex, boolean isFree,
			boolean isSetupPhase) throws CatanException;

	/**
	 * Determines if the player has the resources to build a city
	 * @param playerIndex
	 * @return if the player has the resources to build a city
	 * @throws CatanException
	 */
	public boolean canBuildCity(PlayerNumber playerIndex) throws CatanException;

	/**
	 * Builds a city for the player
	 * @param playerIndex
	 * @param vertex
	 * @return
	 * @throws CatanException
	 */
	public boolean buildCity(PlayerNumber playerIndex, VertexLocation vertex) throws CatanException;

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
	 * Offers a trade for the player
	 * @param invoice
	 * @return
	 * @throws CatanException
	 */
	public boolean offerTrade(ResourceInvoice invoice) throws CatanException;

	/**
	 * Determines if the player can accept a certain trade
	 * @param invoice
	 * @return
	 */
	public boolean canAcceptTrade(ResourceInvoice invoice);

	/**
	 * Accepts a trade for the player
	 * @param invoice
	 * @param willAccept
	 * @return
	 * @throws CatanException
	 */
	public boolean acceptTrade(ResourceInvoice invoice, boolean willAccept) throws CatanException;

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
	 * Performs a maritime trade for the player
	 * @param playerIndex
	 * @param ratio
	 * @param inputResource
	 * @param outputResource
	 * @return
	 * @throws CatanException
	 */
	public boolean maritimeTrade(PlayerNumber playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) throws CatanException;

	/**
	 * Determines if the player needs to discard cards
	 * @param playerIndex
	 * @return
	 */
	public boolean needsToDiscardCards(PlayerNumber playerIndex);

	/**
	 * Discards cards for a player
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
			int wood) throws CatanException;

}
