package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;

/**
 * Manages the game itself, e.g. initializing setup, rolling dice, advancing
 * turns, etc.
 */
public class Game {
	private Board board;
	private Broker broker;
	private Scoreboard scoreboard;
	private Dice dice;
	private Collection<Player> players;
	private PlayerNumber currentPlayer;
	private PostOffice postOffice;

	/**
	 * Initializes the game setup
	 */
	public void init() {
		
	}
	
	/**
	 * Resets the game
	 */
	public void reset() {
		
	}
	
	/**
	 * Returns whether a player can be added to the game
	 * @param color the desired color
	 * @return true if a player can be added to the game
	 */
	public boolean canAddPlayer(CatanColor color) {
		return false;
	}
	
	/**
	 * Adds a player with the desired color to the game
	 * @param color the desired color
	 * @throws CatanException if the desired color is taken or if the game is
	 * full
	 */
	public void addPlayer(CatanColor color) throws CatanException {
		throw new CatanException();
	}
	
	/**
	 * Returns whether an AI can be added to the game
	 * @param color the desired color
	 * @return true if an AI can be added to the game
	 */
	public boolean canAddAI(CatanColor color) {
		return false;
	}
	
	/**
	 * Adds an AI with the desired color to the game
	 * @param color the desired color
	 * @throws CatanException if an AI cannot be added to the game
	 */
	public void addAI(CatanColor color) throws CatanException {
		throw new CatanException();
	}
	
	/**
	 * Rolls the dice
	 */
	public void rollDice() {
		
	}

	/**
	 * Advances the current turn to the next player
	 */
	public void advanceTurn() {

	}
	
	/**
	 * Returns whether the desired player can be robbed
	 * @param player the desired player
	 * @return true if the player can be robbed
	 */
	public boolean canRob(PlayerNumber player) {
		return false;
	}
	
	/**
	 * Robs the desired player
	 * @param player the desired player
	 * @throws CatanException if the desired player cannot be robbed
	 */
	public void rob(PlayerNumber player) throws CatanException {
		throw new CatanException();
	}
}
