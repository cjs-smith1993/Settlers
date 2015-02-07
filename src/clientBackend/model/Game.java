package clientBackend.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import clientBackend.transport.TransportPlayer;
import clientBackend.transport.TransportTurnTracker;
import shared.definitions.*;

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
	private Status status;
	
	/**
	 * Returns the next available player number
	 * */
	private PlayerNumber getNextPlayerNumber() throws CatanException {
		
		for (PlayerNumber number : PlayerNumber.values()) {
			
			if (number == PlayerNumber.BANK) {
				continue;
			}
			
			boolean found = false;
			
			if (players.get(number) != null) {
				found = true;
				continue;
			}
			
			if (!found) {
				return number;
			}
		}
		
		throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "There are no more available player numbers");
	}

	/**
	 * The default constructor for initializing the first game
	 * */
	public Game() {
		players = new HashMap<PlayerNumber, Player>();
		dice = new Dice();
	}
	
	/**
	 * The constructor to be used for any updates to the overall model
	 * */
	public Game(Collection<TransportPlayer> new_players, TransportTurnTracker tracker) throws CatanException {
		
		players = new HashMap<PlayerNumber, Player>();
		
		for (TransportPlayer player : new_players) {
			addPlayer(player);
		}
		
		currentPlayer = tracker.currentTurn;
		currentPlayerHasRolled = ((tracker.status == Status.ROLLING) ? false : true);
		status = tracker.status;
	}
	
	/**
	 * Returns whether a player can be added to the game
	 * To be used at initial game creation
	 * @param user the user to be added
	 * @param color the desired color of the user
	 * @return true if a player can be added to the game
	 */
	public boolean canAddPlayer(User user, CatanColor color) {
		if (players.size() == MAX_PLAYERS) {// Make sure there is room for another player
			return false;
		}
		
		for (Player player : players.values()) {// Make sure the user is unique and the color is available
			if (user.equals(player.getUser()) || color == player.getColor()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Adds a player for the user with the desired color to the game
	 * To be used at initial game creation
	 * @param user the user to be added
	 * @param color the desired color of the user
	 * @throws CatanException if the desired color is taken or if the game is
	 * full
	 */
	public void addPlayer(User user, CatanColor color) throws CatanException {
		if (canAddPlayer(user, color)) {
			PlayerNumber number = getNextPlayerNumber();
			
			players.put(number, new Player(user, color, number));
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot add player with user: " + user + " and color: " + color);
		}
	}
	
	/**
	 * Returns whether a player can be added to the game
	 * To be used throughout the game to update the model
	 * @param user the user to be added
	 * @param color the desired color of the user
	 * @return true if a player can be added to the game
	 */
	public boolean canAddPlayer(TransportPlayer new_player) {
		if (players.size() == MAX_PLAYERS) {// Make sure there is room for another player
			return false;
		}
		
		if (new_player.playerIndex == PlayerNumber.BANK) {// Make sure the player is not the BANK
			return false;
		}
		
		for (Player player : players.values()) {// Make sure the player is unique
			if (new_player.name.equals(player.getUser().getName()) ||
				new_player.color == player.getColor() ||
				new_player.playerIndex == player.getNumber()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Adds a player to the game model
	 * To be used throughout the game to update the model
	 * @param user the user to be added
	 * @param color the desired color of the user
	 * @throws CatanException if the desired color is taken or if the game is
	 * full
	 */
	public void addPlayer(TransportPlayer new_player) throws CatanException {
		if (canAddPlayer(new_player)) {
			players.put(new_player.playerIndex, new Player(new_player));
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Cannot add player. Duplicate player info: " + new_player);
		}
	}
	
	public Road getRoad(PlayerNumber number) throws CatanException {
		if (hasRoad(number)) {
			return players.get(number).getRoad();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Cannot get road. Player " + number + " has no more roads");
		}
	}
	
	public Settlement getSettlement(PlayerNumber number) throws CatanException {
		if (hasSettlement(number)) {
			return players.get(number).getSettlement();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Cannot get settlement. Player " + number + 
					" has no more settlements");
		}
	}
	
	public City getCity(PlayerNumber number) throws CatanException {
		if (hasCity(number)) {
			return players.get(number).getCity();
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Cannot get city. Player " + number + 
					" has no more cities");
		}
	}
	
	public boolean hasRoad(PlayerNumber number) throws CatanException {
		if (players.get(number) == null) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Player " + number + " does not exist");
		}
		return (players.get(number).getNumRoads() > 0);
	}
	
	public boolean hasSettlement(PlayerNumber number) throws CatanException {
		if (players.get(number) == null) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Player " + number + " does not exist");
		}
		return (players.get(number).getNumSettlements() > 0);
	}
	
	public void returnSettlement(PlayerNumber number, Settlement settlement) {
		players.get(number).returnSettlement(settlement);
		return;
	}

	public int getNumSettlements(PlayerNumber number) {
		return players.get(number).getNumSettlements();
	}
	
	public boolean hasCity(PlayerNumber number) throws CatanException {
		if (players.get(number) == null) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, 
					"Player " + number + " does not exist");
		}
		return (players.get(number).getNumCities() > 0);
	}
	
	/**
	 * Rolls the dice
	 */
	public int rollDice() {
		setCurrentPlayerHasRolled(true);
		setLastDiceRoll(dice.roll());
		return lastDiceRoll;
	}

	/**
	 * Advances the current turn to the next player
	 */
	public void advanceTurn() throws CatanException {
		switch (currentPlayer) {
			case ONE:
				currentPlayer = PlayerNumber.TWO;
				break;
			case TWO:
				currentPlayer = PlayerNumber.THREE;
				break;
			case THREE:
				currentPlayer = PlayerNumber.FOUR;
				break;
			case FOUR:
				currentPlayer = PlayerNumber.ONE;
				break;
			default:
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Current player is not a valid PlayerNumber: " + currentPlayer);
		}
	}
	
	public boolean getCurrentPlayerHasRolled() {
		return currentPlayerHasRolled;
	}
	
	public void setCurrentPlayerHasRolled(boolean hasRolled) {
		currentPlayerHasRolled = hasRolled;
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
		return players.size();
	}
	
	public Map<PlayerNumber, Player> getPlayers() {
		return players;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
