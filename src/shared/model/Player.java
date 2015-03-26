package shared.model;

import java.util.ArrayList;

import shared.definitions.*;
import shared.transport.TransportPlayer;

/**
 * Represents a player in the game. A player has a number and color, which
 * uniquely identify him within a game. A player also has collections of
 * available roads, settlements, and cities, which are used to place limitations
 * on the number of properties that can be built by each player
 */
public class Player {
	private ModelUser user;
	private CatanColor color;
	private PlayerNumber number;
	private ArrayList<Road> availableRoads;
	private ArrayList<Settlement> availableSettlements;
	private ArrayList<City> availableCities;
	private boolean hasPlayedDevCard;
	private boolean hasDiscarded;

	public final static int MAX_ROADS = 15;
	public final static int MAX_SETTLEMENTS = 5;
	public final static int MAX_CITIES = 4;

	public Player(ModelUser user, CatanColor color, PlayerNumber number) {
		this.user = user;
		this.color = color;
		this.number = number;

		this.availableRoads = this.initializeRoads(MAX_ROADS);
		this.availableSettlements = this.initializeSettlements(MAX_SETTLEMENTS);
		this.availableCities = this.initializeCities(MAX_CITIES);
	}

	public Player(TransportPlayer player) {
		this.user = new ModelUser(player.name, player.playerID);
		this.color = player.color;
		this.number = player.playerIndex;

		this.availableRoads = this.initializeRoads(player.roads);
		this.availableSettlements = this.initializeSettlements(player.settlements);
		this.availableCities = this.initializeCities(player.cities);

		this.setHasPlayedDevCard(player.playedDevCard);
	}

	/**
	 * Extracts {@link TransportPlayer}
	 *
	 * @param player
	 * @return
	 */
	public TransportPlayer getTransportPlayer(TransportPlayer player) {
		player.name = this.user.getName();
		player.color = this.color;
		player.playerID = this.user.getUserId();
		player.playerIndex = this.number;
		player.roads = this.availableRoads.size();
		player.settlements = this.availableSettlements.size();
		player.cities = this.availableCities.size();
		player.playedDevCard = this.hasPlayedDevCard;
		player.discarded = this.hasDiscarded;

		return player;
	}

	private ArrayList<Road> initializeRoads(int numRoads) {
		ArrayList<Road> roads = new ArrayList<Road>();

		for (int i = 0; i < numRoads; i++) {
			roads.add(new Road(this.number));
		}

		return roads;
	}

	private ArrayList<Settlement> initializeSettlements(int numSettlements) {
		ArrayList<Settlement> settlements = new ArrayList<Settlement>();

		for (int i = 0; i < numSettlements; i++) {
			settlements.add(new Settlement(this.number));
		}

		return settlements;
	}

	private ArrayList<City> initializeCities(int numCities) {
		ArrayList<City> cities = new ArrayList<City>();

		for (int i = 0; i < numCities; i++) {
			cities.add(new City(this.number));
		}

		return cities;
	}

	public int getNumRoads() {
		return this.availableRoads.size();
	}

	public int getNumSettlements() {
		return this.availableSettlements.size();
	}

	public int getNumCities() {
		return this.availableCities.size();
	}

	public void returnSettlement(Settlement settlement) {
		this.availableSettlements.add(settlement);
		return;
	}

	public Road getRoad() throws CatanException {
		if (this.availableRoads.isEmpty()) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Player " + this.number + " has no available roads.");
		}
		return this.availableRoads.remove(0);
	}

	public Settlement getSettlement() throws CatanException {
		if (this.availableRoads.isEmpty()) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Player " + this.number + " has no available settlements.");
		}
		return this.availableSettlements.remove(0);
	}

	public City getCity() throws CatanException {
		if (this.availableCities.isEmpty()) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Player " + this.number + " has no available cities.");
		}
		return this.availableCities.remove(0);
	}

	public ModelUser getUser() {
		return this.user;
	}

	public CatanColor getColor() {
		return this.color;
	}

	public void setColor(CatanColor color) {
		this.color = color;
	}

	public PlayerNumber getNumber() {
		return this.number;
	}

	public boolean hasPlayedDevCard() {
		return this.hasPlayedDevCard;
	}

	public void setHasPlayedDevCard(boolean hasPlayedDevCard) {
		this.hasPlayedDevCard = hasPlayedDevCard;
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
				other.getColor() == this.getColor() && other.getNumber() == this.getNumber());
	}

	public boolean hasDiscarded() {
		return this.hasDiscarded;
	}

	public void setHasDiscarded(boolean hasDiscarded) {
		this.hasDiscarded = hasDiscarded;
	}

}
