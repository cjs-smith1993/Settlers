package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import clientBackend.transport.*;
import shared.definitions.*;
import shared.locations.*;

/**
 * Represents the physical board, including all tiles, chits, harbors, roads,
 * dwellings, and the robber
 */
public class Board {
	private final int RADIUS = 3;

	private Map<Integer, Collection<Chit>> chits;
	private Map<HexLocation, Tile> tiles;
	private Map<EdgeLocation, Road> roads;
	private Map<VertexLocation, Dwelling> dwellings;
	private Collection<Harbor> harbors;
	private Robber robber;

	public Board(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		this.chits = BoardFactory.generateChits(randomNumbers);
		this.tiles = BoardFactory.generateTiles(randomTiles);
		this.roads = new HashMap<EdgeLocation, Road>();
		this.dwellings = new HashMap<VertexLocation, Dwelling>();
		this.harbors = BoardFactory.generateHarbors(randomPorts);
		this.robber = new Robber(BoardFactory.getDesertLocation());
	}

	public Board(TransportMap map) {
		this.chits = BoardFactory.parseChits(map.hexes);
		this.tiles = BoardFactory.parseTiles(map.hexes, map.robber, this.RADIUS);
		this.roads = BoardFactory.parseRoads(map.roads);
		this.dwellings = BoardFactory.parseSettlements(map.settlements);
		this.dwellings.putAll(BoardFactory.parseCities(map.cities));
		this.harbors = BoardFactory.parseHarbors(map.ports);
		this.robber = BoardFactory.parseRobber(map.robber);
	}

	private Collection<Road> getAdjacentRoads(VertexLocation vertex) {
		Collection<Road> adjacentRoads = new ArrayList<Road>();

		Collection<EdgeLocation> adjacentEdges = Geometer.getAdjacentEdges(vertex);
		for (EdgeLocation edge : adjacentEdges) {
			Road road = this.roads.get(edge);
			if (road != null) {
				adjacentRoads.add(road);
			}
		}

		return adjacentRoads;
	}

	private Collection<Road> getAdjacentRoads(EdgeLocation edge) {
		Collection<Road> adjacentRoads = new ArrayList<Road>();

		Collection<EdgeLocation> adjacentEdges = Geometer.getAdjacentEdges(edge);
		for (EdgeLocation e : adjacentEdges) {
			Road road = this.roads.get(e);
			if (road != null) {
				adjacentRoads.add(road);
			}
		}

		return adjacentRoads;
	}

	private Collection<Dwelling> getAdjacentDwellings(HexLocation hex) {
		Collection<Dwelling> adjacentDwellings = new ArrayList<Dwelling>();

		Collection<VertexLocation> adjacentVertices = Geometer.getAdjacentVertices(hex);
		for (VertexLocation vertex : adjacentVertices) {
			Dwelling dwelling = this.dwellings.get(vertex);
			if (dwelling != null) {
				adjacentDwellings.add(dwelling);
			}
		}

		return adjacentDwellings;
	}

	private Collection<Dwelling> getAdjacentDwellings(VertexLocation vertex) {
		Collection<Dwelling> adjacentDwellings = new ArrayList<Dwelling>();

		Collection<VertexLocation> adjacentVertices = Geometer.getAdjacentVertices(vertex);
		for (VertexLocation v : adjacentVertices) {
			Dwelling dwelling = this.dwellings.get(v);
			if (dwelling != null) {
				adjacentDwellings.add(dwelling);
			}
		}

		return adjacentDwellings;
	}

	private Collection<Dwelling> getAdjacentDwellings(EdgeLocation edge) {
		Collection<Dwelling> adjacentDwellings = new ArrayList<Dwelling>();

		Collection<VertexLocation> adjacentVertices = Geometer.getAdjacentVertices(edge);
		for (VertexLocation vertex : adjacentVertices) {
			Dwelling dwelling = this.dwellings.get(vertex);
			if (dwelling != null) {
				adjacentDwellings.add(dwelling);
			}
		}

		return adjacentDwellings;
	}

	private boolean isLand(HexLocation hex) {
		int x = hex.getX();
		int y = hex.getY();
		int z = x + y;

		boolean withinX = Math.abs(x) < this.RADIUS;
		boolean withinY = Math.abs(y) < this.RADIUS;
		boolean withinZ = Math.abs(z) < this.RADIUS;

		return withinX && withinY && withinZ;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean isLand(EdgeLocation edge) {
		HexLocation hex = edge.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		int z = x + y;

		switch (edge.getNormalizedLocation().getDir()) {
		case NorthWest:
			return x > -this.RADIUS && Math.abs(y) < this.RADIUS && z > -this.RADIUS;
		case North:
			return Math.abs(x) < this.RADIUS && y > -this.RADIUS && z > -this.RADIUS;
		case NorthEast:
			return x < this.RADIUS && y > -this.RADIUS && Math.abs(z) < this.RADIUS;
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean isLand(VertexLocation vertex) {
		HexLocation hex = vertex.getNormalizedLocation().getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		int z = x + y;

		switch (vertex.getNormalizedLocation().getDir()) {
		case NorthWest:
			return x > -this.RADIUS && y > -this.RADIUS && z > -this.RADIUS;
		case NorthEast:
			return x < this.RADIUS && y > -this.RADIUS && z > -this.RADIUS;
		}
		return false;
	}

	/**
	 * Returns the current location of the robber
	 *
	 * @return the current location of the robber
	 */
	public HexLocation getRobberLocation() {
		return this.robber.getLocation();
	}

	/**
	 * Returns whether the robber can be moved to the desired location
	 *
	 * @param newLocation
	 *            the desired location
	 * @return true if the robber can be moved to the desired location
	 */
	public boolean canMoveRobber(HexLocation newLocation) {
		HexLocation currentLocation = this.robber.getLocation();
		boolean isLand = this.isLand(newLocation);
		return !currentLocation.equals(newLocation) && isLand;
	}

	/**
	 * Moves the robber to the given hex
	 *
	 * @param newLocation
	 *            the desired new location of the robber
	 * @throws CatanException
	 *             if the robber cannot be moved to the desired location
	 */
	public void moveRobber(HexLocation newLocation) throws CatanException {
		if (this.canMoveRobber(newLocation)) {
			this.robber.setLocation(newLocation);
			Tile tile = this.tiles.get(newLocation);
			if (tile != null) {
				tile.setHasRobber(true);
			}
		}
		else {
			String message = "The robber cannot be moved to " + newLocation.toString();
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, message);
		}
	}

	/**
	 * Generates and returns a collection of invoices corresponding to a given
	 * dice roll. One invoice will be generated for each resource type with a
	 * non-zero count for each player.
	 *
	 * @param number
	 *            the result of the dice roll
	 * @return a collection of invoices consisting of one invoice for each
	 *         resource type with a non-zero count for each player
	 */
	public Collection<ResourceInvoice> generateInvoices(int number) {
		Collection<ResourceInvoice> invoices = new ArrayList<ResourceInvoice>();

		Collection<Chit> chits = this.chits.get(number);
		Collection<Tile> tiles = new ArrayList<Tile>();
		for (Chit chit : chits) {
			HexLocation location = chit.getLocation();
			Tile tile = this.tiles.get(location);
			if (tile != null) {
				tiles.add(tile);
			}
		}

		for (Tile tile : tiles) {
			ResourceType type = tile.getResourceType();
			Collection<Dwelling> dwellings = this.getAdjacentDwellings(tile.getLocation());
			for (Dwelling dwelling : dwellings) {
				int count = dwelling.getVictoryPoints();
				PlayerNumber src = PlayerNumber.BANK;
				PlayerNumber dest = dwelling.getOwner();

				boolean invoiceExists = false;
				for (ResourceInvoice invoice : invoices) {
					if (invoice.getDestinationPlayer() == dest && invoice.getResource() == type) {
						invoiceExists = true;
						invoice.setCount(invoice.getCount() + count);
						break;
					}
				}

				if (!invoiceExists) {
					ResourceInvoice invoice = new ResourceInvoice(type, count, src, dest);
					invoices.add(invoice);
				}
			}
		}

		return invoices;
	}

	/**
	 * Returns whether a road can be built at the desired location
	 *
	 * @param road
	 *            the player's road
	 * @param edge
	 *            the desired location
	 * @param setupPhase
	 *            true if the game is in the setup phase and normal rules do not
	 *            apply
	 * @return true if the road can be built at the desired location
	 */
	public boolean canPlaceRoad(PlayerNumber owner, EdgeLocation edge, boolean setupPhase) {
		// disallow if a road already exists here
		if (this.roads.get(edge) != null) {
			return false;
		}

		// disallow if this edge is not on land
		if (!this.isLand(edge)) {
			return false;
		}

		Collection<Road> connectedRoads = this.getAdjacentRoads(edge);
		Collection<Road> validConnectedRoads = new ArrayList<Road>();
		for (Road r : connectedRoads) {
			if (r.getOwner() == owner) {
				EdgeLocation edge1 = r.getLocation();
				EdgeLocation edge2 = edge;
				VertexLocation sharedVertex = Geometer.getSharedVertex(edge1, edge2);
				if (sharedVertex != null) {
					Dwelling sharedDwelling = this.dwellings.get(sharedVertex);
					if (sharedDwelling == null || sharedDwelling.getOwner() == owner) {
						validConnectedRoads.add(r);
					}
				}
			}
		}

		boolean roadRuleMet = !validConnectedRoads.isEmpty();
		if (setupPhase) {
			Collection<Dwelling> connectedDwellings = this.getAdjacentDwellings(edge);
			ArrayList<Dwelling> ownedDwellings = new ArrayList<Dwelling>();
			for (Dwelling dwelling : connectedDwellings) {
				if (dwelling.getOwner() == owner) {
					ownedDwellings.add(dwelling);
				}
			}

			// the road must be connected to a dwelling the player owns
			if (ownedDwellings.isEmpty()) {
				roadRuleMet = false;
			}
			else {
				// the second road must be attached to the second dwelling
				Dwelling dwelling = ownedDwellings.get(0);
				Collection<Road> otherRoads = this.getAdjacentRoads(dwelling.getLocation());
				roadRuleMet = otherRoads.isEmpty();
			}
		}
		return roadRuleMet;
	}

	/**
	 * Places a road at the given edge
	 *
	 * @param road
	 *            the road to be placed on the board
	 * @param location
	 *            the desired edge for the road
	 * @param setupPhase
	 *            true if the game is in the setup phase and normal rules do not
	 *            apply
	 * @throws CatanException
	 *             if the road cannot be placed at the desired location
	 */
	public void placeRoad(Road road, EdgeLocation edge, boolean setupPhase) throws CatanException {
		if (this.canPlaceRoad(road.getOwner(), edge, setupPhase)) {
			road.setLocation(edge.getNormalizedLocation());
			this.roads.put(edge.getNormalizedLocation(), road);
		}
		else {
			String message = "A road cannot be placed at " + edge.toString();
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, message);
		}
	}

	/**
	 * Returns whether a dwelling can be placed at the desired location
	 *
	 * @param dwelling
	 *            the player's dwelling
	 * @param location
	 *            the desired location
	 * @param setupPhase
	 *            true if the game is in the setup phase and normal rules do not
	 *            apply
	 */
	public boolean canPlaceDwelling(PlayerNumber owner, VertexLocation vertex, boolean setupPhase, PropertyType type) {
		// disallow if the property type is not a settlement or city
		if (type != PropertyType.SETTLEMENT && type != PropertyType.CITY) {
			return false;
		}
		
		Dwelling currentProperty = dwellings.get(vertex);
		// disallow if property is a settlement and a dwelling already exists here
		if (type == PropertyType.SETTLEMENT) {
			if (currentProperty != null) {// I could have combined these but this way is more explicit @kevinjreece
				return false;
			}
		}
		// disallow if property is a city and there is not already one of the owner's settlements at that location
		else if (type == PropertyType.CITY) {
			if (currentProperty == null || currentProperty.getOwner() != owner) {
				return false;
			}
		}

		// disallow if this vertex is not on land
		if (!this.isLand(vertex)) {
			return false;
		}
		
		
		Collection<Dwelling> adjacentDwellings = this.getAdjacentDwellings(vertex);

		Collection<Road> connectedRoads = this.getAdjacentRoads(vertex);
		Collection<Road> validConnectedRoads = new ArrayList<Road>();
		for (Road road : connectedRoads) {
			if (road.getOwner() == owner) {
				validConnectedRoads.add(road);
			}
		}

		boolean distanceRuleMet = adjacentDwellings.isEmpty();
		boolean roadRuleMet = setupPhase || !validConnectedRoads.isEmpty();
		return distanceRuleMet && roadRuleMet;
	}

	/**
	 * Places a dwelling at the given vertex
	 *
	 * @param dwelling
	 *            the dwelling to be placed on the board
	 * @param location
	 *            the desired vertex for the dwelling
	 * @param setupPhase
	 *            true if the game is in the setup phase and normal rules do not
	 *            apply
	 * @throws CatanException
	 *             if the dwelling cannot be placed at the desired location
	 */
	public void placeDwelling(Dwelling dwelling, VertexLocation vertex, boolean setupPhase)
			throws CatanException {
		if (this.canPlaceDwelling(dwelling.getOwner(), vertex, setupPhase, dwelling.getPropertyType())) {
			dwelling.setLocation(vertex.getNormalizedLocation());
			this.dwellings.put(vertex.getNormalizedLocation(), dwelling);
		}
		else {
			String message = "A dwelling cannot be placed at " + vertex.toString();
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, message);
		}
	}

	public Map<PlayerNumber, Collection<Harbor>> getHarborsByPlayer() {
		Map<PlayerNumber, Collection<Harbor>> harborsByPlayer = new HashMap<PlayerNumber, Collection<Harbor>>();

		for (Harbor harbor : this.harbors) {
			for (VertexLocation port : harbor.getPorts()) {
				Dwelling dwelling = this.dwellings.get(port);
				if (dwelling != null) {
					PlayerNumber owner = dwelling.getOwner();
					Collection<Harbor> harborList = harborsByPlayer.get(owner);
					if (harborList == null) {
						harborList = new ArrayList<Harbor>();
						harborsByPlayer.put(owner, harborList);
					}
					harborList.add(harbor);
				}
			}
		}

		return harborsByPlayer;
	}
}





















