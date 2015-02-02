package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;

import clientBackend.transport.TransportPlayer;
import clientBackend.transport.TransportTurnTracker;
import shared.definitions.*;

/**
 * Manages the game itself, e.g. initializing setup, rolling dice, advancing
 * turns, etc.
 */
public class Game {
	private Collection<Player> players;
	private PlayerNumber currentPlayer;
	private Dice dice;
	private static int MAX_PLAYERS = 4;
	
	/**
	 * Returns the next available player number
	 * */
	private PlayerNumber getNextPlayerNumber() throws CatanException {
		
		for (PlayerNumber number : PlayerNumber.values()) {
			boolean found = false;
			
			for (Player player : players) {
				if (player.getNumber() == number) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				return number;
			}
		}
		
		throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "There are no more available player numbers");
	}

	/**
	 * The default consructor for initializing the first game
	 * */
	public Game() {
		players = new ArrayList<Player>();
		dice = new Dice();
	}
	
	/**
	 * The constructor to be used for any updates to the overall model
	 * */
	public Game(Collection<TransportPlayer> players, TransportTurnTracker tracker) throws CatanException {
		
		for (TransportPlayer player : players) {
			addPlayer(new Player(new User(player.name, player.playerID), player.color, player.playerIndex));
		}
		
		currentPlayer = tracker.currentTurn;
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
		
		for (Player player : players) {// Make sure the user is unique and the color is available
			if (user == player.getUser() || color == player.getColor()) {
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
			players.add(new Player(user, color, getNextPlayerNumber()));
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
	public boolean canAddPlayer(Player new_player) {
		if (players.size() == MAX_PLAYERS) {// Make sure there is room for another player
			return false;
		}
		
		for (Player player : players) {// Make sure the player is unique
			if (new_player.equals(player)) {
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
	public void addPlayer(Player new_player) throws CatanException {
		if (canAddPlayer(new_player)) {
			players.add(new_player);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot add player: " + new_player);
		}
	}
	
	/**
	 * Returns whether an AI can be added to the game
	 * @param color the desired color
	 * @return true if an AI can be added to the game
	 */
	public boolean canAddAI(CatanColor color) {
		//TODO
		return false;
	}
	
	/**
	 * Adds an AI with the desired color to the game
	 * @param color the desired color
	 * @throws CatanException if an AI cannot be added to the game
	 */
	public void addAI(CatanColor color) throws CatanException {
		if (canAddAI(color)) {
			//TODO
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot add AI: " + color);
		}
	}
	
	/**
	 * Rolls the dice
	 */
	public int rollDice() {
		return dice.roll();
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
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Current is not a valid PlayerNumber: " + currentPlayer);
		}
	}
	
}
