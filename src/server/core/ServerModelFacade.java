package server.core;

import java.util.ArrayList;
import java.util.Map;

import shared.definitions.CatanState;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.*;
import shared.model.facade.AbstractModelFacade;
import shared.transport.TransportModel;
import shared.transport.TransportPlayer;
import shared.transport.TransportTurnTracker;

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

	public TransportModel getModel() {
		TransportModel transportModel = new TransportModel();
		
		transportModel.bank = broker.getTransportBank();
		transportModel.chat = postOffice.getTransportChat();
		transportModel.log = postOffice.getTransportLog();
		transportModel.deck = broker.getTransportDeck();
		transportModel.map = board.getTransportMap();
		
		TransportTurnTracker turnTracker = new TransportTurnTracker();
		game.getTransportTurnTracker(turnTracker);
		scoreboard.getTransportTurnTracker(turnTracker);
		transportModel.turnTracker = turnTracker;
		
		transportModel.tradeOffer = broker.getTransportTradeOffer();
		transportModel.players = getTransportPlayers();
		transportModel.version = version;
		transportModel.winner = winnerServerID;
		
		return transportModel;
	}
	
	public TransportPlayer[] getTransportPlayers() {
		ArrayList<TransportPlayer> transportPlayers = new ArrayList<>();
		Map<PlayerNumber, Player> players = game.getPlayers();
		
		for (Map.Entry<PlayerNumber, Player> player : players.entrySet()) {
			TransportPlayer transportPlayer = new TransportPlayer();
			
			transportPlayer = scoreboard.getTransportPlayer(transportPlayer, player.getKey());
			transportPlayer = player.getValue().getTransportPlayer(transportPlayer);
			transportPlayer = broker.getTransportPlayer(transportPlayer, player.getKey());
			
			transportPlayers.add(transportPlayer);
		}
		
		return (TransportPlayer[]) transportPlayers.toArray();
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
