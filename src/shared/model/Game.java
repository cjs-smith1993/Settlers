package shared.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import shared.definitions.*;
import shared.transport.TransportPlayer;
import shared.transport.TransportTurnTracker;

/**
 * Manages the game itself, e.g. initializing setup, rolling dice, advancing
 * turns, etc.
 */
public class Game {
	private Dice dice;
	private int lastDiceRoll;
	private static int MAX_PLAYERS = 4;
	private PlayerNumber currentPlayer;
	private boolean currentPlayerHasRolled = false;
	private Map<PlayerNumber, Player> players;
	private CatanState state;
	
	
	public void setPlayers(Map<PlayerNumber, Player> players) {
		this.players = players;
	}
	/**
	 * The default constructor for initializing the first game
	 * */
	public Game() {
		this.players = new HashMap<PlayerNumber, Player>();
		this.dice = new Dice();
		this.setState(CatanState.FIRST_ROUND);
		this.currentPlayer = PlayerNumber.ONE;
	}

	/**
	 * The constructor to be used for any updates to the overall model
	 * */
	public Game(Collection<TransportPlayer> new_players, TransportTurnTracker tracker)
			throws CatanException {

		this.players = new HashMap<PlayerNumber, Player>();

		for (TransportPlayer player : new_players) {
			if (player != null) {
				this.addPlayer(player);
			}
		}

		this.currentPlayer = tracker.currentTurn;
		this.currentPlayerHasRolled = ((tracker.status == CatanState.ROLLING) ? false : true);
		this.state = tracker.status;

		this.dice = new Dice();
	}

	/**
	 * IMPORTANT: This must be called just before or just after
	 * getTransporTracker() is called on the Scoreboard class. It requires both
	 * classes to populate all the information.
	 *
	 * @param tracker
	 * @return TransportTurnTracker tracker
	 */
	public TransportTurnTracker getTransportTurnTracker(TransportTurnTracker tracker) {
		tracker.currentTurn = this.currentPlayer;
		tracker.status = this.state;

		return tracker;
	}

	/**
	 * Returns the next available player number
	 **/
	private PlayerNumber getNextPlayerNumber() throws CatanException {

		for (PlayerNumber number : PlayerNumber.values()) {

			if (number == PlayerNumber.BANK) {
				continue;
			}

			boolean found = false;

			if (this.players.get(number) != null) {
				found = true;
				continue;
			}

			if (!found) {
				return number;
			}
		}

		throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
				"There are no more available player numbers");
	}

	/**
	 * Returns whether a player can be added to the game To be used at initial
	 * game creation
	 *
	 * @param user
	 *            the user to be added
	 * @param color
	 *            the desired color of the user
	 * @return true if a player can be added to the game
	 */
	public boolean canAddPlayer(ModelUser user, CatanColor color) {
		if (this.players.size() == MAX_PLAYERS) {// Make sure there is room for another player
			return false;
		}

		for (Player player : this.players.values()) {// Make sure the user is unique and the color is available
			if (user.equals(player.getUser()) || color == player.getColor()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Adds a player for the user with the desired color to the game To be used
	 * at initial game creation
	 *
	 * @param user
	 *            the user to be added
	 * @param color
	 *            the desired color of the user
	 * @throws CatanException
	 *             if the desired color is taken or if the game is full
	 */
	public void addPlayer(ModelUser user, CatanColor color) throws CatanException {
		if (this.canAddPlayer(user, color)) {
			PlayerNumber number = this.getNextPlayerNumber();

			this.players.put(number, new Player(user, color, number));
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot add player with user: " + user + " and color: " + color);
		}
	}

	/**
	 * Returns whether a player can be added to the game To be used throughout
	 * the game to update the model
	 *
	 * @param user
	 *            the user to be added
	 * @param color
	 *            the desired color of the user
	 * @return true if a player can be added to the game
	 */
	public boolean canAddPlayer(TransportPlayer new_player) {
		if (this.players.size() == MAX_PLAYERS) {// Make sure there is room for another player
			return false;
		}

		if (new_player.playerIndex == PlayerNumber.BANK) {// Make sure the player is not the BANK
			return false;
		}

		for (Player player : this.players.values()) {// Make sure the player is unique
			if (new_player.name.equals(player.getUser().getName()) ||
					new_player.color == player.getColor() ||
					new_player.playerIndex == player.getNumber()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Adds a player to the game model To be used throughout the game to update
	 * the model
	 *
	 * @param user
	 *            the user to be added
	 * @param color
	 *            the desired color of the user
	 * @throws CatanException
	 *             if the desired color is taken or if the game is full
	 */
	public void addPlayer(TransportPlayer new_player) throws CatanException {
		if (this.canAddPlayer(new_player)) {
			this.players.put(new_player.playerIndex, new Player(new_player));
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot add player. Duplicate player info: " + new_player.name);
		}
	}

	public Road getRoad(PlayerNumber number) throws CatanException {
		if (this.hasRoad(number)) {
			return this.players.get(number).getRoad();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot get road. Player " + number + " has no more roads");
		}
	}

	public Settlement getSettlement(PlayerNumber number) throws CatanException {
		if (this.hasSettlement(number)) {
			return this.players.get(number).getSettlement();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot get settlement. Player " + number +
							" has no more settlements");
		}
	}

	public City getCity(PlayerNumber number) throws CatanException {
		if (this.hasCity(number)) {
			return this.players.get(number).getCity();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot get city. Player " + number +
							" has no more cities");
		}
	}

	public boolean hasRoad(PlayerNumber number) throws CatanException {
		if (this.players.get(number) == null) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Player " + number + " does not exist");
		}
		return (this.players.get(number).getNumRoads() > 0);
	}

	public int getNumRoads(PlayerNumber player) {
		return this.players.get(player).getNumRoads();
	}

	public boolean hasSettlement(PlayerNumber number) throws CatanException {
		if (this.players.get(number) == null) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Player " + number + " does not exist");
		}
		return (this.players.get(number).getNumSettlements() > 0);
	}

	public void returnSettlement(PlayerNumber number, Settlement settlement) {
		this.players.get(number).returnSettlement(settlement);
		return;
	}

	public int getNumSettlements(PlayerNumber number) {
		return this.players.get(number).getNumSettlements();
	}

	public boolean hasCity(PlayerNumber number) throws CatanException {
		if (this.players.get(number) == null) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Player " + number + " does not exist");
		}
		return (this.players.get(number).getNumCities() > 0);
	}

	public boolean hasPlayedDevCard(PlayerNumber player) {
		return this.players.get(player).hasPlayedDevCard();
	}

	/**
	 * Rolls the dice
	 */
	public int rollDice() {
		this.setCurrentPlayerHasRolled(true);
		this.setLastDiceRoll(this.dice.roll());
		return this.lastDiceRoll;
	}

	/**
	 * Advances the current turn to the next player
	 */
	public void advanceTurn() throws CatanException {
		switch (this.currentPlayer) {
		case ONE:
			this.currentPlayer = PlayerNumber.TWO;
			break;
		case TWO:
			this.currentPlayer = PlayerNumber.THREE;
			break;
		case THREE:
			this.currentPlayer = PlayerNumber.FOUR;
			break;
		case FOUR:
			this.currentPlayer = PlayerNumber.ONE;
			break;
		default:
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Current player is not a valid PlayerNumber: " + this.currentPlayer);
		}
	}

	public void decrementTurn() throws CatanException {
		switch (this.currentPlayer) {
		case TWO:
			this.currentPlayer = PlayerNumber.ONE;
			break;
		case THREE:
			this.currentPlayer = PlayerNumber.TWO;
			break;
		case FOUR:
			this.currentPlayer = PlayerNumber.THREE;
			break;
		default:
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Current player is not a valid PlayerNumber: " + this.currentPlayer);
		}
	}

	public boolean getCurrentPlayerHasRolled() {
		return this.currentPlayerHasRolled;
	}

	public void setCurrentPlayerHasRolled(boolean hasRolled) {
		this.currentPlayerHasRolled = hasRolled;
	}

	public PlayerNumber getCurrentPlayer() {
		return this.currentPlayer;
	}

	public int getLastDiceRoll() {
		return this.lastDiceRoll;
	}

	public void setLastDiceRoll(int roll) {
		this.lastDiceRoll = roll;
	}

	public int getNumPlayers() {
		return this.players.size();
	}

	public Map<PlayerNumber, Player> getPlayers() {
		return this.players;
	}

	public CatanState getState() {
		return this.state;
	}

	public void setState(CatanState state) {
		this.state = state;
	}

	public boolean hasDiscarded(PlayerNumber playerIndex) {
		return this.players.get(playerIndex).hasDiscarded();
	}

	public void setHasDiscarded(PlayerNumber playerIndex, boolean hasDiscarded) {
		this.players.get(playerIndex).setHasDiscarded(hasDiscarded);
	}

	public void setHasPlayedDevCard(PlayerNumber playerIndex, boolean hasPlayed) {
		this.players.get(playerIndex).setHasPlayedDevCard(hasPlayed);
	}
}
