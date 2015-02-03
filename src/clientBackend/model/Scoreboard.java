package clientBackend.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.definitions.*;
import clientBackend.transport.TransportMap;
import clientBackend.transport.TransportPlayer;
import clientBackend.transport.TransportRoad;
import clientBackend.transport.TransportTurnTracker;

/**
 * The scoreboard manages the victory points of all players in the game. Each
 * time a player performs an action that can affect victory points (e.g.
 * building a settlement, upgrading a settlement to a city, building a road,
 * or playing a development card), the scoreboard will update
 */
public class Scoreboard {
	private PlayerNumber largestArmy;
	private PlayerNumber longestRoad;
	private Map<PlayerNumber, Integer> points;
	private Map<PlayerNumber, Integer> activeKnights;
	private Map<PlayerNumber, Integer> builtRoads;
	
	public Scoreboard(List<TransportPlayer> player, TransportTurnTracker turnTracker) {
		initializeMap(points);
		initializeMap(activeKnights);
		initializeMap(builtRoads);
		
		largestArmy = turnTracker.largestArmy;
		longestRoad = turnTracker.longestRoad;
		
		countRoads(player);
		countKnights(player);
		countPoints(player);
	}
	
	public Scoreboard() {
		initializeMap(points);
		initializeMap(activeKnights);
		initializeMap(builtRoads);
		
		largestArmy = PlayerNumber.BANK;
		longestRoad = PlayerNumber.BANK;
	}
	
	private void initializeMap(Map<PlayerNumber, Integer> map) {
		map = new HashMap<PlayerNumber, Integer>();
		
		for (int i = 0; i < 5; i++) {
			map.put(PlayerNumber.values()[i], 0);
		}
	}
	
	private void countRoads(List<TransportPlayer> players) {		
		for (int i = 0; i < players.size(); i++) {
			builtRoads.put(players.get(i).playerIndex, players.get(i).roads);
		}
	}
	
	private void countKnights(List<TransportPlayer> players) {
		for (int i = 0; i < players.size(); i++) {
			builtRoads.put(players.get(i).playerIndex, players.get(i).soldiers);
		}
	}
	
	private void countPoints(List<TransportPlayer> players) {
		for (int i = 0; i < players.size(); i++) {
			points.put(players.get(i).playerIndex, players.get(i).victoryPoints);
		}
	}
	
	private void updatePlayerMaps(Map<PlayerNumber, Integer> playerMap, PlayerNumber player, int difference) {
		int currentValue = playerMap.get(player);
		currentValue += difference;
		playerMap.put(player, currentValue);
	}
	
	/**
	 * Adds one victory point to the player who built the dwelling
	 * @param player the player who built the dwelling
	 */
	public void dwellingBuilt(PlayerNumber player) {
		updatePlayerMaps(points, player, 1);
	}
	
	/**
	 * Adds a road to a player, then recalculates the owner of the longest road and adjusts victory points,
	 * if necessary.
	 */
	public void roadBuilt(PlayerNumber player) {
		int playerRoads = builtRoads.get(player);
		playerRoads++;
		builtRoads.put(player, playerRoads);
		
		calculateSpecialCardHolder(builtRoads, longestRoad);
	}
	
	/**
	 * This method calculates the PlayerNumber with the largest Integer as a value in the map.
	 * This method works with LongestRoad and LargestArmy.
	 * @param specialCard
	 * @param currentHolder
	 */
	private void calculateSpecialCardHolder(Map<PlayerNumber, Integer> specialCard, PlayerNumber currentHolder) {
		PlayerNumber currentWinner = currentHolder;
		PlayerNumber oldWinner = currentWinner;
		
		for (Map.Entry<PlayerNumber, Integer> cardCount : specialCard.entrySet()) {
			if (cardCount.getValue() >= 5) {
				if (cardCount.getValue() > specialCard.get(currentWinner)) {
					currentWinner = cardCount.getKey();
				}
			}
		}
		
		if (oldWinner != currentWinner) {
			updatePlayerMaps(points, oldWinner, -2);
			updatePlayerMaps(points, currentWinner, 2);
		}
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
		switch (type) {
		case MONUMENT:
			updatePlayerMaps(points, player, 1);
			break;
		case SOLDIER:
			updatePlayerMaps(activeKnights, player, 1);
			calculateSpecialCardHolder(activeKnights, largestArmy);
			break;
		case ROAD_BUILD:
			updatePlayerMaps(builtRoads, player, 1);
			calculateSpecialCardHolder(builtRoads, longestRoad);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Returns the number of victory points for the player
	 * @param player the desired player
	 * @return the number of victory points for the player
	 */
	public int getPoints(PlayerNumber player) {
		return points.get(player);
	}
}
