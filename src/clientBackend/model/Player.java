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
	private CatanColor color;
	private PlayerNumber number;
	private Collection<Road> availableRoads;
	private Collection<Settlement> availableSettlements;
	private Collection<City> availableCities;
	
	public Player(User user, CatanColor color, PlayerNumber number) {
		this.user = user;
		this.color = color;
		this.number = number;
	}
	
	public User getUser() {
		return user;
	}
	
	public CatanColor getColor() {
		return color;
	}
	
	public PlayerNumber getNumber() {
		return number;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		
		Player other = (Player) o;
		
		return (other.getUser() == this.getUser() &&
				other.getColor() == this.getColor() &&
				other.getNumber() == this.getNumber());
	}
}
