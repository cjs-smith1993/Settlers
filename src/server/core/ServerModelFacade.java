package server.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import server.commands.ICommand;
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
import shared.transport.TransportTradeOffer;
import shared.transport.TransportTurnTracker;

/**
 * The facade that the Game Manager will be using to update and interact with
 * different games.
 *
 */
public class ServerModelFacade extends AbstractModelFacade {
	private int gameId;
	private String name;
	private Collection<ICommand> commandsList;
	private boolean randomTiles;
	private boolean randomNumbers;
	private boolean randomPorts;

	public ServerModelFacade(
			int gameId,
			String name,
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts) {

		RandomNumberGenerator.getInstance(gameId).reSeed(gameId);
		this.gameId = gameId;
		this.name = name;
		this.board = new Board(randomTiles, randomNumbers, randomPorts);
		this.game = new Game();
		this.broker = new Broker();
		this.broker.setRandomSeed(this.gameId);
		this.postOffice = new PostOffice();
		this.scoreboard = new Scoreboard();
		this.openOffer = null;
		this.commandsList = new ArrayList<ICommand>();

		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
	}

	public ServerModelFacade(String fileName) throws IOException, CatanException {
		RandomNumberGenerator.getInstance(this.gameId).reSeed(this.gameId);
		this.initializeModelFromFile(fileName);
		this.broker.setRandomSeed(this.gameId);
		this.commandsList = new ArrayList<ICommand>();
	}

	private void incrementVersion() {
		this.version++;
	}

	public void initializeModel(TransportModel newModel) throws CatanException {
		super.initializeModel(newModel);
		this.gameId = newModel.gameId;
		this.name = newModel.name;
	}

	public TransportModel getModel(int version) {
		if (this.version != version) {
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

		transportModel.tradeOffer = this.makeTransTrade(this.openOffer);
		transportModel.players = this.getTransportPlayers();
		transportModel.version = this.version;

		this.winnerServerID = this.scoreboard.getWinner().getInteger();

		transportModel.winner = this.winnerServerID;
		transportModel.gameId = this.gameId;

		return transportModel;
	}

	private TransportTradeOffer makeTransTrade(ResourceInvoice invoice) {
		TransportTradeOffer tradeOffer = new TransportTradeOffer();
		if (invoice == null) {
			return null;
		}
		tradeOffer.receiver = invoice.destinationPlayer;
		tradeOffer.sender = invoice.sourcePlayer;

		tradeOffer.offer.brick = invoice.getBrick();
		tradeOffer.offer.ore = invoice.getOre();
		tradeOffer.offer.sheep = invoice.getSheep();
		tradeOffer.offer.wheat = invoice.getWheat();
		tradeOffer.offer.wood = invoice.getWood();
		return tradeOffer;
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

	public Collection<ICommand> getCommands() {
		return this.commandsList;
	}

	public TransportModel postCommands(Collection<ICommand> commandsList) {
		this.commandsList = commandsList;
		for (ICommand cmd : commandsList) {
			cmd.execute();
		}

		return this.getModel();
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
		this.incrementVersion();
		return this.getModel();
	}

	private void sendLog(PlayerNumber playerIndex, String content) {
		String name = this.getNameForPlayerNumber(playerIndex);
		this.postOffice.addLogMessage(new Message(name, content));
		this.incrementVersion();
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

			String name = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, name + " rolled a " + numberRolled);
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "cannot roll");
		}
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
			if (this.canPlaceRobber(playerIndex, newLocation)) {
				this.board.moveRobber(newLocation);

				ResourceInvoice invoice = this.broker.randomRobPlayer(playerIndex, victim);
				this.game.setState(CatanState.PLAYING);

				String playerName = this.getNameForPlayerNumber(playerIndex);
				String victimName = this.getNameForPlayerNumber(victim);
				if (invoice != null) {
					this.broker.processInvoice(invoice);
					this.sendLog(playerIndex, playerName + " robbed " + victimName);
				}
				else {
					this.sendLog(playerIndex, playerName + " robbed no one!");
				}
				return this.getModel();

			}
			else {
				throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
						"Cannot place robber at that location.");
			}

		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"CurrentPlayer or State. is not correct");
		}
	}

	public TransportModel finishTurn(PlayerNumber playerIndex) throws CatanException {
		if (this.canFinishTurn(playerIndex)) {
			this.game.setCurrentPlayerHasRolled(false);

			if (this.game.getState() == CatanState.PLAYING) {
				this.game.setState(CatanState.ROLLING);
				this.game.advanceTurn();
			}
			else if (this.game.getState() == CatanState.FIRST_ROUND) {
				if (this.game.getCurrentPlayer() == PlayerNumber.FOUR) {
					this.game.setState(CatanState.SECOND_ROUND);
				}
				else {
					this.game.advanceTurn();
				}
			}
			else if (this.game.getState() == CatanState.SECOND_ROUND) {
				if (this.game.getCurrentPlayer() == PlayerNumber.ONE) {
					this.game.setState(CatanState.ROLLING);
				}
				else {
					this.game.decrementTurn();
				}
			}
			this.broker.makeDevelopmentCardsPlayable(playerIndex);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " ended their turn");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are either not the player "
							+ "who's turn it is, or you still need you finish your turn.");
		}
	}

	public TransportModel buyDevCard(PlayerNumber playerIndex) throws CatanException {
		// TODO Auto-generated method stub
		if (this.canBuyDevCard(playerIndex)) {
			this.broker.purchase(playerIndex, PropertyType.DEVELOPMENT_CARD);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " bought a development card");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "cannot buy dev card");
		}
	}

	public TransportModel useYearOfPlenty(PlayerNumber playerIndex,
			ResourceType resource1, ResourceType resource2) throws CatanException {
		if (this.canUseYearOfPlenty(playerIndex)) {
			this.broker.processYearOfPlenty(playerIndex, resource1, resource2);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " played a Year of Plenty card");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are not qualified to use the Year Of Plenty card. Repent.");
		}
	}

	public TransportModel useRoadBuilding(PlayerNumber playerIndex,
			EdgeLocation edge1, EdgeLocation edge2) throws CatanException {
		if (this.canUseRoadBuilding(playerIndex)) {
			this.buildRoad(playerIndex, edge1, true);
			this.buildRoad(playerIndex, edge2, true);
			this.broker.processRoadBuilding(playerIndex);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " played a Road Building card");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are not qualified to use the Road Building card. Repent.");
		}
	}

	public TransportModel useSoldier(PlayerNumber playerIndex,
			PlayerNumber victim, HexLocation newLocation) throws CatanException {
		if (this.canUseSoldier(playerIndex)) {
			this.broker.processSoldier(playerIndex);
			this.robPlayer(playerIndex, victim, newLocation);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			String victimName = this.getNameForPlayerNumber(victim);
			this.sendLog(playerIndex, playerName + " played a Soldier card and robbed "
					+ victimName);
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are not qualified to use the Soldier card. Repent.");
		}
	}

	public TransportModel useMonopoly(PlayerNumber playerIndex, ResourceType resource)
			throws CatanException {
		if (this.canUseMonopoly(playerIndex)) {
			this.broker.processMonopoly(playerIndex, resource);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " played a Monopoly card");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are not qualified to use the Monopoly card. Repent.");
		}
	}

	public TransportModel useMonument(PlayerNumber playerIndex) throws CatanException {
		if (this.canUseMonument(playerIndex)) {
			this.broker.processMonument(playerIndex);
			this.scoreboard.devCardPlayed(playerIndex, DevCardType.MONUMENT);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " played a Monument card");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
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
			Road road = this.game.getRoad(playerIndex);
			this.board.placeRoad(road, location, isFree);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " built a road");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
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
			Settlement settlement = this.game.getSettlement(playerIndex);
			this.board.placeSettlement(settlement, vertex, isFree);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " built a settlement");
			this.broker.setPlayersHarbors(playerIndex, this.board.getHarborsByPlayer().get(playerIndex));
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are not qualified to use buildSettlement. Repent.");
		}

	}

	public TransportModel buildCity(PlayerNumber playerIndex, VertexLocation vertex)
			throws CatanException {
		if (this.canBuildCity(playerIndex)) {
			this.broker.purchase(playerIndex, PropertyType.CITY);
			this.scoreboard.dwellingBuilt(playerIndex);
			City city = this.game.getCity(playerIndex);
			Settlement settlement = (Settlement) this.board.placeCity(city, vertex, false);
			this.game.returnSettlement(playerIndex, settlement);

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " upgraded a settlement to a city");
			return this.getModel();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"You are not qualified to use buildCity. Repent.");
		}
	}

	public TransportModel offerTrade(ResourceInvoice invoice) throws CatanException {
		if (this.canOfferTrade(invoice)) {
			this.openOffer = invoice;

			PlayerNumber sourceIndex = invoice.sourcePlayer;
			PlayerNumber destinationIndex = invoice.destinationPlayer;
			String sourceName = this.getNameForPlayerNumber(sourceIndex);
			String destinationName = this.getNameForPlayerNumber(destinationIndex);
			this.sendLog(sourceIndex, sourceName + " offered a trade to " + destinationName);
			return this.getModel();
		}
		else {
			this.openOffer = null;
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"it is not your turn or you can't offer that trade.");
		}
	}

	public TransportModel acceptTrade(int acceptingPlayerId, boolean willAccept)
			throws CatanException {

		PlayerNumber sourceIndex = this.openOffer.sourcePlayer;
		PlayerNumber destinationIndex = this.openOffer.destinationPlayer;
		String sourceName = this.getNameForPlayerNumber(sourceIndex);
		String destinationName = this.getNameForPlayerNumber(destinationIndex);

		if (willAccept) {
			if (this.canAcceptTrade(this.openOffer)) {
				ResourceInvoice invoice = this.openOffer;
				if (willAccept) {
					this.broker.processInvoice(invoice);
					this.openOffer = null;
					this.sendLog(sourceIndex, destinationName + " accepted a trade from "
							+ sourceName);
				}
				else {
					this.sendLog(this.openOffer.getSourcePlayer(), "Trade was declined");
				}
			}
			else {
				throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "cannot accept a trade");
			}
		}
		else {
			this.openOffer = null;
			this.sendLog(sourceIndex, destinationName + " could not accept a trade from "
					+ sourceName);
		}
		return this.getModel();
	}

	public TransportModel maritimeTrade(PlayerNumber playerIndex, int ratio,
			ResourceType inputResource, ResourceType outputResource) throws CatanException {
		if (this.broker.canMaritimeTrade(playerIndex, inputResource)) {
			ResourceInvoice invoice = new ResourceInvoice(playerIndex, PlayerNumber.BANK);

			for (ResourceType type : ResourceType.values()) {
				switch (type) {
				case BRICK:
					if (type == inputResource) {
						invoice.setBrick(ratio);
					}
					if (type == outputResource) {
						invoice.setBrick(-1);
					}
					break;
				case WOOD:
					if (type == inputResource) {
						invoice.setWood(ratio);
					}
					if (type == outputResource) {
						invoice.setWood(-1);
					}
					break;
				case WHEAT:
					if (type == inputResource) {
						invoice.setWheat(ratio);
					}
					if (type == outputResource) {
						invoice.setWheat(-1);
					}
					break;
				case SHEEP:
					if (type == inputResource) {
						invoice.setSheep(ratio);
					}
					if (type == outputResource) {
						invoice.setSheep(-1);
					}
					break;
				case ORE:
					if (type == inputResource) {
						invoice.setOre(ratio);
					}
					if (type == outputResource) {
						invoice.setOre(-1);
					}
					break;
				default:
					break;
				}
			}
			this.broker.processInvoice(invoice);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Can not maritime trade.");
		}
		return this.getModel();
	}

	public TransportModel discardCards(PlayerNumber playerIndex, int brick, int ore,
			int sheep, int wheat, int wood) throws CatanException {

		if (this.needsToDiscardCards(playerIndex)) {
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

			String playerName = this.getNameForPlayerNumber(playerIndex);
			this.sendLog(playerIndex, playerName + " discarded cards");
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
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
		this.incrementVersion();
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
		this.incrementVersion();
	}

	public int getGameId() {
		return this.gameId;
	}

	public String getName() {
		return this.name;
	}

	public boolean getRandomTiles() {
		return this.randomTiles;
	}

	public boolean getRandomNumbers() {
		return this.randomNumbers;
	}

	public boolean getRandomPorts() {
		return this.randomPorts;
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
