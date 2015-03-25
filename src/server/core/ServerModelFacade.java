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
		
		return transportPlayers.toArray(new TransportPlayer[transportPlayers.size()]);
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
		
		return this.getModel();
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
			this.game.setCurrentPlayerHasRolled(true);
			this.version++;
			
			String name = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, name + " rolled a " + Integer.toString(numberRolled));
			
			if (numberRolled == 7) {
				this.startDiscarding();
				if (!this.continueDiscarding()) {
					this.stopDiscarding();
				}
			}
			else {
				Collection<ResourceInvoice> invoices = this.board.generateInvoices(numberRolled);
				
				for (ResourceInvoice resourceInvoice : invoices) {
					this.broker.processInvoice(resourceInvoice);
				}
				
				this.game.setState(CatanState.PLAYING);
			}
		}

		return this.getModel();
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
			this.game.setCurrentPlayerHasRolled(false);
			this.game.setState(CatanState.ROLLING);
			this.game.advanceTurn();
			this.broker.makeDevelopmentCardsPlayable(playerIndex);
			
			return this.getModel();
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

	public TransportModel useMonopoly(PlayerNumber player, ResourceType resource) throws CatanException {
		if (canUseMonopoly(player)) {
			broker.processMonopoly(player, resource);
			
			return getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "You are not qualified to use the Monopoly card. Repent.")
		}
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
			this.game.setHasDiscarded(playerIndex, true);
			
			if (!this.continueDiscarding()) {
				this.stopDiscarding();
			}
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "User attempted to discard an invalid number of cards.");
		}
		
		return getModel();
	}

	/**
	 * Called when a 7 is rolled to determine if any players need to discard.
	 * Checks the number of cards each player has and sets hasDiscarded to true 
	 * for each player that doesn't need to discard.
	 * @pre model state is ROLLING
	 * @return
	 */
	public void startDiscarding() {
		for (PlayerNumber playerIndex : PlayerNumber.values()) {
			if (playerIndex != PlayerNumber.BANK) {
				if (this.broker.getNumberToDiscard(playerIndex) == 0) {
					this.game.setHasDiscarded(playerIndex, true);
				}
			}
		}
		
		this.game.setState(CatanState.DISCARDING);
		this.version++;
	}
	
	/**
	 * Determines if any players need to discard.
	 * Checks hasDiscarded for each player.
	 * @pre model state is DISCARDING
	 * @return
	 */
	public boolean continueDiscarding() {
		boolean isNecessary = false;
		
		for (PlayerNumber playerIndex : PlayerNumber.values()) {
			if (playerIndex != PlayerNumber.BANK) {
				if (!this.game.hasDiscarded(playerIndex)) {
					isNecessary = true;
				}
			}
		}
		
		return isNecessary;
	}
	
	/**
	 * Called when no more players need to discard.
	 * Sets hasDiscarded to false for each player.
	 * Sets the model state to ROBBING.
	 * Increments the model version.
	 * @pre model state is DISCARDING
	 */
	public void stopDiscarding() {
		for (PlayerNumber playerIndex : PlayerNumber.values()) {
			if (playerIndex != PlayerNumber.BANK) {
				this.game.setHasDiscarded(playerIndex, false);
			}
		}
		this.game.setState(CatanState.ROBBING);
		this.version++;
	}
}
