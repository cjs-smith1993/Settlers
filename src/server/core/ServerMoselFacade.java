package server.core;

import java.util.Collection;

import client.backend.dataTransportObjects.DTOGame;
import shared.ModelFacade;
import shared.definitions.AIType;
import shared.definitions.CatanColor;
import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ResourceInvoice;

/**
 * The facade that the Game Manager will be useig to update and interact with different games.
 *
 */
public class ServerMoselFacade extends ModelFacade {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean register(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DTOGame> getGamesList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DTOGame createGame(boolean randomTiles, boolean randomNumbers,
			boolean randomPorts, String gameName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean joinGame(int gameId, CatanColor desiredColor) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveGame(int gameId, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean loadGame(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getModel(boolean sendVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetGame() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAI(AIType AItype) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<AIType> getAITypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sendChat(PlayerNumber playerIndex, String content) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canRollNumber(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rollNumber(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPlaceRobber(PlayerNumber playerNumber,
			HexLocation location, CatanState state) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canRobPlayer(PlayerNumber playerIndex,
			PlayerNumber victimIndex, CatanState state) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean robPlayer(PlayerNumber playerIndex, PlayerNumber victim,
			HexLocation newLocation, CatanState state) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canFinishTurn(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean finishTurn(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canBuyDevCard(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyDevCard(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUseYearOfPlenty(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPlayYearOfPlenty(PlayerNumber playerIndex,
			ResourceType resource1, ResourceType resource2) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useYearOfPlenty(PlayerNumber playerIndex,
			ResourceType resource1, ResourceType resource2) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUseRoadBuilding(PlayerNumber player) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useRoadBuilding(PlayerNumber playerIndex,
			EdgeLocation edge1, EdgeLocation edge2) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUseSoldier(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useSoldier(PlayerNumber playerIndex,
			PlayerNumber victimIndex, HexLocation newLocation) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUseMonopoly(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useMonopoly(PlayerNumber playerIndex, ResourceType resource) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUseMonument(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useMonument(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canBuildRoad(PlayerNumber playerIndex, boolean isFree) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buildRoad(PlayerNumber playerIndex, EdgeLocation location,
			boolean isFree, boolean isSetupPhase) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canBuildSettlement(PlayerNumber playerIndex, boolean isFree) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buildSettlement(PlayerNumber playerIndex,
			VertexLocation vertex, boolean isFree, boolean isSetupPhase) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canBuildCity(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buildCity(PlayerNumber playerIndex, VertexLocation vertex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPlaceRoad(PlayerNumber playerIndex, EdgeLocation edge,
			boolean isSetupPhase) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPlaceSettlement(PlayerNumber playerIndex,
			VertexLocation vertex, boolean isSetupPhase) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPlaceCity(PlayerNumber playerIndex, VertexLocation vertex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOfferTrade(ResourceInvoice invoice) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean offerTrade(ResourceInvoice invoice) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canAcceptTrade(ResourceInvoice invoice) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptTrade(ResourceInvoice invoice, boolean willAccept) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canMaritimeTrade(PlayerNumber playerIndex,
			ResourceType giving) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean maritimeTrade(PlayerNumber playerIndex, int ratio,
			ResourceType inputResource, ResourceType outputResource) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needsToDiscardCards(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean discardCards(PlayerNumber playerIndex, int brick, int ore,
			int sheep, int wheat, int wood) {
		// TODO Auto-generated method stub
		return false;
	}

}
