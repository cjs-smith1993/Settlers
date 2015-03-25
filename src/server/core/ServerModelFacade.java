package server.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOPlayer;
import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
import shared.definitions.CatanState;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
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
	private int gameId;
	private String name;

	public ServerModelFacade(
			int gameId,
			String name,
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts) {
		this.gameId = gameId;
		this.name = name;
		this.board = new Board(randomTiles, randomNumbers, randomPorts);
		this.game = new Game();
		this.broker = new Broker();
		this.postOffice = new PostOffice();
		this.scoreboard = new Scoreboard();
		this.openOffer = null;
	}

	public ServerModelFacade(String fileName) throws IOException, CatanException {
		this.initializeModelFromFile(fileName);
	}

	public TransportModel getModel(int version) {
		if (this.version == version) {
			return this.getModel();
		}

		return null;
	}

	public TransportModel getModel() {
		TransportModel transportModel = new TransportModel();

		transportModel.bank = this.broker.getTransportBank();
		transportModel.chat = this.postOffice.getTransportChat();
		transportModel.log = this.postOffice.getTransportLog();
		transportModel.deck = this.broker.getTransportDeck();
		transportModel.map = this.board.getTransportMap();

		TransportTurnTracker turnTracker = new TransportTurnTracker();
		this.game.getTransportTurnTracker(turnTracker);
		this.scoreboard.getTransportTurnTracker(turnTracker);
		transportModel.turnTracker = turnTracker;

		transportModel.tradeOffer = this.broker.getTransportTradeOffer();
		transportModel.players = this.getTransportPlayers();
		transportModel.version = this.version;
		transportModel.winner = this.winnerServerID;

		return transportModel;
	}

	public TransportPlayer[] getTransportPlayers() {
		ArrayList<TransportPlayer> transportPlayers = new ArrayList<>();
		Map<PlayerNumber, Player> players = this.game.getPlayers();

		for (Map.Entry<PlayerNumber, Player> player : players.entrySet()) {
			TransportPlayer transportPlayer = new TransportPlayer();

			transportPlayer = this.scoreboard.getTransportPlayer(transportPlayer, player.getKey());
			transportPlayer = player.getValue().getTransportPlayer(transportPlayer);
			transportPlayer = this.broker.getTransportPlayer(transportPlayer, player.getKey());

			transportPlayers.add(transportPlayer);
		}

		return transportPlayers.toArray(new TransportPlayer[transportPlayers.size()]);
	}

	public void resetGame() {
		// TODO: Implement this.
	}

	public boolean joinGame(ModelUser user, CatanColor color) throws CatanException {
		this.game.addPlayer(user, color);
		return true;
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

	public TransportModel rollNumber(PlayerNumber playerIndex, int numberRolled)
			throws CatanException {
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
		if (this.canRobPlayer(playerIndex, victim)) {
			if (this.board.canMoveRobber(newLocation)) {
				this.board.moveRobber(newLocation);

				if (this.broker.getResourceCardCount(victim, ResourceType.ALL) > 0) {
					ResourceInvoice invoice = this.broker.randomRobPlayer(playerIndex, victim);

					if (invoice != null) {
						this.broker.processInvoice(invoice);
					}
				}

				return this.getModel();
			}
			else {
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"Cannot place robber at that location.");
			}

		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"CurrentPlayer or State. is not correct");
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
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are either not the player "
							+ "who's turn it is, or you still need you finish your turn.");
		}
	}

	public TransportModel buyDevCard(PlayerNumber playerIndex) throws CatanException {
		// TODO Auto-generated method stub
		this.broker.purchase(playerIndex, PropertyType.DEVELOPMENT_CARD);
		return this.getModel();
	}

	public TransportModel useYearOfPlenty(PlayerNumber playerIndex,
			ResourceType resource1, ResourceType resource2) throws CatanException {
		if (this.canUseYearOfPlenty(playerIndex)) {
			this.broker.processYearOfPlenty(playerIndex, resource1, resource2);
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use the Year Of Plenty card. Repent.");
		}
	}

	public TransportModel useRoadBuilding(PlayerNumber playerIndex,
			EdgeLocation edge1, EdgeLocation edge2) throws CatanException {
		if (this.canUseRoadBuilding(playerIndex)) {
			this.buildRoad(playerIndex, edge1, true);
			this.buildRoad(playerIndex, edge2, true);
			this.broker.processRoadBuilding(playerIndex);

			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use the Road Building card. Repent.");
		}
	}

	public TransportModel useSoldier(PlayerNumber playerIndex,
			PlayerNumber victim, HexLocation newLocation) throws CatanException {
		// TODO: Implement safety checks:
		// 1 - Check whether it's this player's turn.
		// 2 - Check whether the player has a soldier card to spend.
		// 3 - Expire dev card.
		return this.robPlayer(playerIndex, victim, newLocation);
	}

	public TransportModel useMonopoly(PlayerNumber playerIndex, ResourceType resource)
			throws CatanException {
		if (this.canUseMonopoly(playerIndex)) {
			this.broker.processMonopoly(playerIndex, resource);

			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use the Monopoly card. Repent.");
		}
	}

	public TransportModel useMonument(PlayerNumber playerIndex) throws CatanException {
		if (this.canUseMonument(playerIndex)) {
			this.broker.processMonument(playerIndex);
			this.scoreboard.devCardPlayed(playerIndex, DevCardType.MONUMENT);

			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use the Monument card. Repent.");
		}
	}

	public TransportModel buildRoad(PlayerNumber playerIndex, EdgeLocation location,
			boolean isFree) throws CatanException {
		if (this.canBuildRoad(playerIndex, isFree)) {
			if (!isFree) {
				this.broker.purchase(playerIndex, PropertyType.ROAD);
			}

			this.scoreboard.roadBuilt(playerIndex);
			this.game.purchaseProperty(playerIndex, PropertyType.ROAD);

			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use buildRoad. Repent.");
		}
	}

	public TransportModel buildSettlement(PlayerNumber playerIndex,
			VertexLocation vertex, boolean isFree) throws CatanException {
		if (this.canBuildSettlement(playerIndex, isFree)) {
			if (!isFree) {
				this.broker.purchase(playerIndex, PropertyType.SETTLEMENT);
			}

			this.scoreboard.dwellingBuilt(playerIndex);
			this.game.purchaseProperty(playerIndex, PropertyType.SETTLEMENT);

			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use buildSettlement. Repent.");
		}

	}

	public TransportModel buildCity(PlayerNumber playerIndex, VertexLocation vertex)
			throws CatanException {
		if (this.canBuildCity(playerIndex)) {
			this.broker.purchase(playerIndex, PropertyType.CITY);
			this.scoreboard.dwellingBuilt(playerIndex);
			this.game.purchaseProperty(playerIndex, PropertyType.CITY);

			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"You are not qualified to use buildCity. Repent.");
		}
	}

	public TransportModel offerTrade(ResourceInvoice invoice) throws CatanException {
		if (this.canOfferTrade(invoice)) {
			this.openOffer = invoice;
		}
		else {
			this.openOffer = null;
			this.version++;
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"it is not your turn or you can't offer that trade.");
		}
		return this.getModel();
	}

	public TransportModel acceptTrade(int acceptingPlayerId, boolean willAccept) {
		if (this.canAcceptTrade(this.openOffer) && willAccept) {

		}
		else {
			this.openOffer = null;
			this.version++;
		}
		return this.getModel();
	}

	public TransportModel maritimeTrade(PlayerNumber playerIndex, int ratio,
			ResourceType inputResource, ResourceType outputResource) {
		// TODO Auto-generated method stub
		return this.getModel();
	}

	public TransportModel discardCards(PlayerNumber playerIndex, int brick, int ore,
			int sheep, int wheat, int wood) throws CatanException {

		int numberOfDiscardedResources = brick + ore + sheep + wheat + wood;

		if (this.game.getState() == CatanState.DISCARDING
				&& this.broker.getNumberToDiscard(playerIndex) == numberOfDiscardedResources) {
			ResourceInvoice invoice = new ResourceInvoice(playerIndex, PlayerNumber.BANK);

			invoice.setBrick(brick);
			invoice.setOre(ore);
			invoice.setSheep(sheep);
			invoice.setWheat(wheat);
			invoice.setWood(wood);

			this.broker.processInvoice(invoice);
			this.game.setHasDiscarded(playerIndex, true);

			if (!this.continueDiscarding()) {
				this.stopDiscarding();
			}
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"User attempted to discard an invalid number of cards.");
		}

		return this.getModel();
	}

	/**
	 * Called when a 7 is rolled to determine if any players need to discard.
	 * Checks the number of cards each player has and sets hasDiscarded to true
	 * for each player that doesn't need to discard.
	 *
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
	 * Determines if any players need to discard. Checks hasDiscarded for each
	 * player.
	 *
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
	 * Called when no more players need to discard. Sets hasDiscarded to false
	 * for each player. Sets the model state to ROBBING. Increments the model
	 * version.
	 *
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

	public int getGameId() {
		return this.gameId;
	}

	public String getName() {
		return this.name;
	}

	public DTOGame getGameInfo() {
		ArrayList<DTOPlayer> players = new ArrayList<DTOPlayer>();
		for (Player player : this.game.getPlayers().values()) {
			CatanColor color = player.getColor();
			ModelUser user = player.getUser();
			players.add(new DTOPlayer(color, user.getName(), user.getUserId()));
		}
		DTOGame gameInfo = new DTOGame(this.getGameId(), this.getName(), players);
		return gameInfo;
	}

}
