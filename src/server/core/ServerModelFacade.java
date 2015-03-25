package server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
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
	
	public TransportModel getModel(int version) {
		if (this.version == version) {
			return getModel();
		}
		
		return null;
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
	
	public void resetGame() {
		// TODO: Implement this.
	}
	
	public boolean joinGame(ModelUser user, CatanColor color) {
		return false;
	}

	public TransportModel sendChat(PlayerNumber playerIndex, String content) {
		String name = this.getNameForPlayerNumber(playerIndex);
		this.postOffice.addChatMessage(new Message(name, content));
		
		return getModel();
	}
	
	private void sendLog(PlayerNumber playerIndex, String content) {
		String name = this.getNameForPlayerNumber(playerIndex);
		this.postOffice.addLogMessage(new Message(name, content));
	}

	public TransportModel rollNumber(PlayerNumber playerIndex, int numberRolled) throws CatanException {
		// If 7, change state to discarding for those that need to discard.
			// If people need to discard:
				// Set CatanState to DISCARDING.
				// Keep a list of people that are discarding.
			// Move to Robbing.
		// Else, Map --> Resource Invoices --> Broker.
			// Change state to Playing.
		
		if (this.canRollNumber(playerIndex)) {
			game.setCurrentPlayerHasRolled(true);
			version++;
			
			if (numberRolled == 7) {
				if (broker.checkDiscardStatus()) {
					// The discardCards() method will setState() to .ROBBING
					// when the number of people who need to discard is fulfilled.
					game.setState(CatanState.DISCARDING);
				}
			}
			else {
				Collection<ResourceInvoice> invoices = board.generateInvoices(numberRolled);
				
				for (ResourceInvoice resourceInvoice : invoices) {
					broker.processInvoice(resourceInvoice);
				}
				
				game.setState(CatanState.PLAYING);
			}
		}

		// TODO: Create log messageS, not message.
		
		return getModel();
	}

	public boolean canPlaceRobber(PlayerNumber playerIndex, HexLocation location) {
		if (super.canPlaceRobber(playerIndex, location)
				&& (this.game.getState() == CatanState.ROBBING
				|| this.game.getState() == CatanState.PLAYING)) {
			return true;
		}
		
		return false;
	}

	public boolean canRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex) {
		if (super.canRobPlayer(playerIndex, victimIndex)
				&& (this.game.getState() == CatanState.ROBBING
				|| this.game.getState() == CatanState.PLAYING)) {
			return true;
		}

		return false;
	}

	public TransportModel robPlayer(PlayerNumber playerIndex, PlayerNumber victim,
			HexLocation newLocation) throws CatanException {
		// TODO: Should canRobPlayer() use CatanState as an argument or not?
		if (canRobPlayer(playerIndex, victim)) {
			if (board.canMoveRobber(newLocation)) {
				board.moveRobber(newLocation);
				
				if (broker.getResourceCardCount(victim, ResourceType.ALL) > 0) {
					ResourceInvoice invoice = broker.randomRobPlayer(playerIndex, victim);
					
					if (invoice != null) {
						broker.processInvoice(invoice);
					}
				}
				
				return getModel();
			}
			else {
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot place robber at that location.");
			}
			
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "CurrentPlayer or State. is not correct");
		}
	}

	public TransportModel finishTurn(PlayerNumber playerIndex) throws CatanException {
		if (this.canFinishTurn(playerIndex)) {
			game.setCurrentPlayerHasRolled(false);
			game.setState(CatanState.ROLLING);
			broker.makeDevelopmentCardsPlayable(playerIndex);
			
			return getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "You are either not the player "
					+ "who's turn it is, or you still need you finish your turn.");
		}
	}

	public TransportModel buyDevCard(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel useYearOfPlenty(PlayerNumber playerIndex,
			ResourceType resource1, ResourceType resource2) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel useRoadBuilding(PlayerNumber playerIndex,
			EdgeLocation edge1, EdgeLocation edge2) {
		// TODO Auto-generated method stub
		//check state
		return getModel();
	}

	public TransportModel useSoldier(PlayerNumber playerIndex,
			PlayerNumber victim, HexLocation newLocation) throws CatanException {
		// TODO: Implement safety checks:
			// 1 - Check whether it's this player's turn.
			// 2 - Check whether the player has a soldier card to spend.
			// 3 - Expire dev card.
		return robPlayer(playerIndex, victim, newLocation);
	}

	public TransportModel useMonopoly(PlayerNumber playerIndex, ResourceType resource) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel useMonument(PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel buildRoad(PlayerNumber playerIndex, EdgeLocation location,
			boolean isFree) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel buildSettlement(PlayerNumber playerIndex,
			VertexLocation vertex, boolean isFree) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel buildCity(PlayerNumber playerIndex, VertexLocation vertex) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel offerTrade(ResourceInvoice invoice) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel acceptTrade(int acceptingPlayerId, boolean willAccept) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel maritimeTrade(PlayerNumber playerIndex, int ratio,
			ResourceType inputResource, ResourceType outputResource) {
		// TODO Auto-generated method stub
		return getModel();
	}

	public TransportModel discardCards(PlayerNumber playerIndex, int brick, int ore,
			int sheep, int wheat, int wood) throws CatanException {
		
		int numberOfDiscardedResources = brick + ore + sheep + wheat + wood;
		
		if (game.getState() == CatanState.DISCARDING 
				&& broker.getNumberToDiscard(playerIndex) == numberOfDiscardedResources) {
			ResourceInvoice invoice = new ResourceInvoice(playerIndex, PlayerNumber.BANK);
			
			invoice.setBrick(brick);
			invoice.setOre(ore);
			invoice.setSheep(sheep);
			invoice.setWheat(wheat);
			invoice.setWood(wood);
			
			broker.processInvoice(invoice);
			
			if (!broker.checkDiscardStatus()) {
				version++;
				game.setState(CatanState.ROBBING);
			}
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "User attempted to discard an invalid number of cards.");
		}
		
		return getModel();
	}
}
