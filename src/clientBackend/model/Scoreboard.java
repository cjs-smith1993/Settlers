package clientBackend.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shared.definitions.*;
import clientBackend.transport.TransportPlayer;
import clientBackend.transport.TransportTurnTracker;

/**
 * The scoreboard manages the victory points of all players in the game. Each
 * time a player performs an action that can affect victory points (e.g.
 * building a settlement, upgrading a settlement to a city, building a road, or
 * playing a development card), the scoreboard will update
 */
public class Scoreboard {
	private PlayerNumber largestArmy;
	private PlayerNumber longestRoad;
	private Map<PlayerNumber, Integer> points;
	private Map<PlayerNumber, Integer> activeKnights;
	private Map<PlayerNumber, Integer> builtRoads;

	public Scoreboard(List<TransportPlayer> player, TransportTurnTracker turnTracker) {
		this.points = this.initializeMap();
		this.activeKnights = this.initializeMap();
		this.builtRoads = this.initializeMap();

		this.largestArmy = turnTracker.largestArmy;
		this.longestRoad = turnTracker.longestRoad;

		this.countRoads(player);
		this.countKnights(player);
		this.countPoints(player);
	}

	public Scoreboard() {
		this.points = this.initializeMap();
		this.activeKnights = this.initializeMap();
		this.builtRoads = this.initializeMap();

		this.largestArmy = PlayerNumber.BANK;
		this.longestRoad = PlayerNumber.BANK;
	}

	private Map<PlayerNumber, Integer> initializeMap() {
		HashMap<PlayerNumber, Integer> map = new HashMap<PlayerNumber, Integer>();

		for (int i = 0; i < 4; i++) {
			map.put(PlayerNumber.values()[i], 0);
		}

		return map;
	}

	private void countRoads(List<TransportPlayer> players) {
		for (TransportPlayer player : players) {
			if (player == null) {
				continue;
			}
			PlayerNumber number = player.playerIndex;
			int roads = player.roads;
			this.builtRoads.put(number, roads);
		}
	}

	private void countKnights(List<TransportPlayer> players) {
		for (TransportPlayer player : players) {
			if (player == null) {
				continue;
			}
			this.activeKnights.put(player.playerIndex, player.soldiers);
		}
	}

	private void countPoints(List<TransportPlayer> players) {
		for (TransportPlayer player : players) {
			if (player == null) {
				continue;
			}
			this.points.put(player.playerIndex, player.victoryPoints);
		}
	}

	private void updatePlayerMaps(Map<PlayerNumber, Integer> playerMap, PlayerNumber player,
			int difference) {
		int currentValue = playerMap.get(player);
		currentValue += difference;
		playerMap.put(player, currentValue);
	}

	/**
	 * Adds one victory point to the player who built the dwelling
	 *
	 * @param player
	 *            the player who built the dwelling
	 */
	public void dwellingBuilt(PlayerNumber player) {
		this.updatePlayerMaps(this.points, player, 1);
	}

	/**
	 * Adds a road to a player, then recalculates the owner of the longest road
	 * and adjusts victory points, if necessary.
	 */
	public void roadBuilt(PlayerNumber player) {
		int playerRoads = this.builtRoads.get(player);
		playerRoads++;
		this.builtRoads.put(player, playerRoads);

		this.calculateSpecialCardHolder(this.builtRoads, this.longestRoad);
	}

	/**
	 * This method calculates the PlayerNumber with the largest Integer as a
	 * value in the map. This method works with LongestRoad and LargestArmy.
	 *
	 * @param specialCard
	 * @param currentHolder
	 */
	private void calculateSpecialCardHolder(Map<PlayerNumber, Integer> specialCard,
			PlayerNumber currentHolder) {
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
			this.updatePlayerMaps(this.points, oldWinner, -2);
			this.updatePlayerMaps(this.points, currentWinner, 2);
		}
	}

	/**
	 * If the card is a monument card, the player's victory points is
	 * incremented. If the card is a soldier or road building, the owner of the
	 * longest road or largest army, respectively, is recalculated and victory
	 * points are adjusted, if necessary.
	 *
	 * @param player
	 * @param type
	 */
	public void devCardPlayed(PlayerNumber player, DevCardType type) {
		switch (type) {
		case MONUMENT:
			this.updatePlayerMaps(this.points, player, 1);
			break;
		case SOLDIER:
			this.updatePlayerMaps(this.activeKnights, player, 1);
			this.calculateSpecialCardHolder(this.activeKnights, this.largestArmy);
			break;
		case ROAD_BUILD:
			this.updatePlayerMaps(this.builtRoads, player, 1);
			this.calculateSpecialCardHolder(this.builtRoads, this.longestRoad);
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the number of victory points for the player
	 *
	 * @param player
	 *            the desired player
	 * @return the number of victory points for the player
	 */
	public int getPoints(PlayerNumber player) {
		return this.points.get(player);
	}
}
