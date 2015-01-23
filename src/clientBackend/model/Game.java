package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;

/**
 * Manages the game itself, e.g. initializing setup, TODO
 */
public class Game {
	private Board board;
	private Broker broker;
	private Scoreboard scoreboard;
	private Dice dice;
	private Collection<Player> players;
	private PlayerNumber currentPlayer;

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
	 * Adds a player with the desired color to the game
	 * @param color the desired color
	 */
	public void addPlayer(CatanColor color) {
		
	}
	
	/**
	 * Adds an AI with the desired color to the game
	 * @param color
	 */
	public void addAI(CatanColor color) {
		
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
	 * Robs the selected player TODO
	 * @param player
	 */
	public void rob(PlayerNumber player) {

	}
}
