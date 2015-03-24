package server.core;

import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.*;
import shared.model.Broker;
import shared.model.Game;
import shared.model.PostOffice;
import shared.model.ResourceInvoice;
import shared.model.Scoreboard;
import shared.model.facade.AbstractModelFacade;

/**
 * The facade that the Game Manager will be using to update and interact with
 * different games.
 *
 */
public class ServerModelFacade extends AbstractModelFacade {
	public ServerModelFacade(boolean randomTiles, boolean randomNumbers,
			boolean randomPorts) {
		this.board = new Board(randomTiles, randomNumbers, randomPorts);
		this.game = new Game();
		this.broker = new Broker();
		this.postOffice = new PostOffice();
		this.scoreboard = new Scoreboard();
		this.openOffer = null;
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
	public boolean sendChat(PlayerNumber playerIndex, String content) {
		String name = this.getNameForPlayerNumber(playerIndex);
		this.postOffice.addChatMessage(new Message(name, content));
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
	public boolean canPlaceRobber(PlayerNumber playerIndex, HexLocation location, CatanState state) {

		if (super.canPlaceRobber(playerIndex, location)
				&& (this.game.getState() == CatanState.ROBBING
				|| this.game.getState() == CatanState.PLAYING)) {
			return true;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex, CatanState state) {

		if (super.canRobPlayer(playerIndex, victimIndex)
				&& (this.game.getState() == CatanState.ROBBING
				|| this.game.getState() == CatanState.PLAYING)) {
			return true;
		}

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
	public boolean finishTurn(PlayerNumber playerIndex) {
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
	public boolean useYearOfPlenty(PlayerNumber playerIndex,
			ResourceType resource1, ResourceType resource2) {
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
	public boolean useSoldier(PlayerNumber playerIndex,
			PlayerNumber victimIndex, HexLocation newLocation) {
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
	public boolean useMonument(PlayerNumber playerIndex) {
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
	public boolean buildSettlement(PlayerNumber playerIndex,
			VertexLocation vertex, boolean isFree, boolean isSetupPhase) {
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
	public boolean offerTrade(ResourceInvoice invoice) {
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
	public boolean maritimeTrade(PlayerNumber playerIndex, int ratio,
			ResourceType inputResource, ResourceType outputResource) {
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
