package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.*;

/**
 * Manages the game itself, e.g. initializing setup, rolling dice, advancing
 * turns, etc.
 */
public class Game {
	private Collection<Player> players;
	private PlayerNumber currentPlayer;
//	private RandomNumberGenerator rng; Can we do this either with a static method or use a Singleton?
	private Dice dice;
	private static int MAX_PLAYERS = 4;

	public Game() {
		players = new ArrayList<Player>();
		dice = new Dice();
	}
	
	/**
	 * Initializes the game setup
	 */
	public void init() {
		//TODO
	}
	
	/**
	 * Resets the game
	 */
	public void reset() {
		//TODO
	}
	
	/**
	 * Returns whether a player can be added to the game
	 * @param color the desired color
	 * @return true if a player can be added to the game
	 */
	public boolean canAddPlayer(CatanColor color) {
		if (players.size() == MAX_PLAYERS) {// Make sure there is room for another player
			return false;
		}
		
		for (Player player : players) {// Make sure the color is available
			if (player.getColor() == color) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Adds a player with the desired color to the game
	 * @param color the desired color
	 * @throws CatanException if the desired color is taken or if the game is
	 * full
	 */
	public void addPlayer(CatanColor color) throws CatanException {
		if (canAddPlayer(color)) {
			//TODO
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot add player: " + color);
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
