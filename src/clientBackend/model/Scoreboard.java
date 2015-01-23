package clientBackend.model;

import java.util.Map;

import shared.definitions.*;

/**
 * The scoreboard manages the victory points of all players in the game. Each
 * time a player performs an action that can affect victory points (e.g.
 * building a settlement, upgrading a settlement to a city, building a road,
 * or playing a development card), the scoreboard will update
 */
public class Scoreboard {
	private Map<PlayerNumber, Integer> points;
	private PlayerNumber largestArmy;
	private PlayerNumber longestRoad;
	
	/**
	 * Adds one victory point to the player who built the dwelling
	 * @param player the player who built the dwelling
	 */
	public void dwellingBuilt(PlayerNumber player) {
		
	}
	
	/**
	 * Recalculates the owner of the longest road and adjusts victory points,
	 * if necessary
	 */
	public void roadBuilt() {
		
	}
	
	/**
	 * If the card is a monument card, the player's victory points is
	 * incremented. If the card is a soldier or road building, the owner of the
	 * longest road or largest army, respectively, is recalculated and victory
	 * points are adjusted, if necessary.
	 * @param player
	 * @param type
	 */
	public void devCardPlayed(PlayerNumber player, DevCardType type) {
		
	}
	
	/**
	 * Returns the number of victory points for the player
	 * @param player the desired player
	 * @return the number of victory points for the player
	 */
	public int getPoints(PlayerNumber player) {
		return 0;
	}
}
