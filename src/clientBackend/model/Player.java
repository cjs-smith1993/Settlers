package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;

/**
 * Represents a player in the game. A player has a number and color, which
 * uniquely identify him within a game. A player also has collections of
 * available roads, settlements, and cities, which are used to place
 * limitations on the number of properties that can be built by each player 
 */
public class Player {
	private User user;
	private PlayerNumber number;
	private CatanColor color;
	private Collection<Road> availableRoads;
	private Collection<Settlement> availableSettlements;
	private Collection<City> availableCities;
}
