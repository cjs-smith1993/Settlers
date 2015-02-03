package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents the physical board, including all tiles, chits, harbors, roads,
 * dwellings, and the robber
 */
public class Board {
	private final int NUM_WOOD = 4;
	private final int NUM_SHEEP = 4;
	private final int NUM_WHEAT = 4;
	private final int NUM_BRICK = 3;
	private final int NUM_ORE = 3;
	private final int NUM_DESERT = 1;
	private final int NUM_WATER = 18;
	private final int NUM_CHITS = 18;
	private final int RADIUS = 3;

	private Map<HexLocation, Tile> tiles;
	private Collection<Harbor> harbors;
	private Map<Integer, Collection<Chit>> chits;
	private PlayerNumber longestRoad;
	private Robber robber;
	private Map<EdgeLocation, Road> roads;
	private Map<VertexLocation, Dwelling> dwellings;

	public Board(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		// create tiles
		this.tiles = new HashMap<HexLocation, Tile>();

		ArrayList<Tile> woodTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_WOOD; i++) {
			woodTiles.add(new Tile(null, ResourceType.WOOD, false));
		}
		ArrayList<Tile> sheepTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_SHEEP; i++) {
			sheepTiles.add(new Tile(null, ResourceType.SHEEP, false));
		}
		ArrayList<Tile> wheatTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_WHEAT; i++) {
			wheatTiles.add(new Tile(null, ResourceType.WHEAT, false));
		}
		ArrayList<Tile> brickTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_BRICK; i++) {
			brickTiles.add(new Tile(null, ResourceType.BRICK, false));
		}
		ArrayList<Tile> oreTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_ORE; i++) {
			oreTiles.add(new Tile(null, ResourceType.ORE, false));
		}
		ArrayList<Tile> desertTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_DESERT; i++) {
			desertTiles.add(new Tile(null, ResourceType.NONE, false));
		}
		ArrayList<Tile> waterTiles = new ArrayList<Tile>();
		for (int i = 0; i < this.NUM_WATER; i++) {
			waterTiles.add(new Tile(null, ResourceType.ALL, false));
		}

		// create chits
		this.chits = new HashMap<Integer, Collection<Chit>>();

		Chit a = new Chit('A', 5, null);
		Chit b = new Chit('B', 2, null);
		Chit c = new Chit('C', 6, null);
		Chit d = new Chit('D', 3, null);
		Chit e = new Chit('E', 8, null);
		Chit f = new Chit('F', 10, null);
		Chit g = new Chit('G', 9, null);
		Chit h = new Chit('H', 12, null);
		Chit i = new Chit('I', 11, null);
		Chit j = new Chit('J', 4, null);
		Chit k = new Chit('K', 8, null);
		Chit l = new Chit('L', 10, null);
		Chit m = new Chit('M', 9, null);
		Chit n = new Chit('N', 4, null);
		Chit o = new Chit('O', 5, null);
		Chit p = new Chit('P', 6, null);
		Chit q = new Chit('Q', 3, null);
		Chit r = new Chit('R', 11, null);

		ArrayList<Chit> twos = new ArrayList<Chit>();
		ArrayList<Chit> threes = new ArrayList<Chit>();
		ArrayList<Chit> fours = new ArrayList<Chit>();
		ArrayList<Chit> fives = new ArrayList<Chit>();
		ArrayList<Chit> sixes = new ArrayList<Chit>();
		ArrayList<Chit> eights = new ArrayList<Chit>();
		ArrayList<Chit> nines = new ArrayList<Chit>();
		ArrayList<Chit> tens = new ArrayList<Chit>();
		ArrayList<Chit> elevens = new ArrayList<Chit>();
		ArrayList<Chit> twelves = new ArrayList<Chit>();

		twos.add(b);
		threes.add(d);
		threes.add(q);
		fours.add(j);
		fours.add(n);
		fives.add(a);
		fives.add(o);
		sixes.add(c);
		sixes.add(p);
		eights.add(e);
		eights.add(k);
		nines.add(g);
		nines.add(m);
		tens.add(f);
		tens.add(l);
		elevens.add(i);
		elevens.add(r);
		twelves.add(h);

		if (randomTiles) {

		}
		else {
			Tile tile;
			HexLocation loc;

			tile = woodTiles.remove(0);
			loc = new HexLocation(0, 2);
			tile.setLocation(loc);
			this.tiles.put(loc, tile);

			tile = oreTiles.remove(0);
			loc = new HexLocation(-1, -1);
			tile.setLocation(loc);
			this.tiles.put(loc, tile);
		}

		if (randomNumbers) {

		}
		else {
			eights.get(0).setTile(this.tiles.get(new HexLocation(0, 2)));
			eights.get(1).setTile(this.tiles.get(new HexLocation(-1, -1)));
		}

		if (randomPorts) {

		}
		else {

		}

		for (int z = 0; z < eights.size(); z++) {
			Tile t = eights.get(z).getTile();
			System.out.println(t.getLocation() + " " + t.getResourceType());
		}
	}

	public static void main(String args[]) {
		Board board = new Board(false, false, false);
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

	@SuppressWarnings("incomplete-switch")
	private boolean isLand(EdgeLocation edge) {
		HexLocation hex = edge.getHexLoc();
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
		HexLocation hex = vertex.getHexLoc();
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
	 * Returns whether the robber can be moved to the desired location
	 *
	 * @param location
	 *            the desired location
	 * @return true if the robber can be moved to the desired location
	 */
	public boolean canMoveRobber(Tile newLocation) {
		Tile currentLocation = this.robber.getLocation();
		return currentLocation != newLocation;
	}

	/**
	 * Moves the robber to the given vertex
	 *
	 * @param location
	 *            the desired new location of the robber
	 * @throws CatanException
	 *             if the robber cannot be moved to the desired location
	 */
	public void moveRobber(Tile newLocation) throws CatanException {
		if (this.canMoveRobber(newLocation)) {
			this.robber.setLocation(newLocation);
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
			tiles.add(chit.getTile());
		}

		for (Tile tile : tiles) {
			ResourceType type = tile.getResourceType();
			Collection<Dwelling> dwellings = tile.getConnectedDwellings();
			for (Dwelling dwelling : dwellings) {
				int count = dwelling.getVictoryPoints();
				PlayerNumber src = PlayerNumber.BANK;
				PlayerNumber dest = dwelling.getOwner();

				boolean invoiceExists = false;
				for (ResourceInvoice invoice : invoices) {
					if (invoice.getDestinationPlayer() == dest) {
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
	 * @param location
	 *            the desired location
	 * @return true if the road can be built at the desired location
	 */
	public boolean canPlaceRoad(Road road, EdgeLocation edge) {
		// disallow if a road already exists here
		if (this.roads.get(edge) != null) {
			return false;
		}

		// disallow if this edge is not on land
		if (!this.isLand(edge)) {
			return false;
		}

		PlayerNumber owner = road.getOwner();
		Collection<Road> connectedRoads = this.getAdjacentRoads(edge);
		Collection<Road> validConnectedRoads = new ArrayList<Road>();
		for (Road r : connectedRoads) {
			if (r.getOwner() == owner) {
				EdgeLocation edge1 = r.getLocation();
				EdgeLocation edge2 = road.getLocation();
				VertexLocation sharedVertex = Geometer.getSharedVertex(edge1, edge2);
				if (sharedVertex != null) {
					Dwelling sharedDwelling = this.dwellings.get(sharedVertex);
					if (sharedDwelling == null || sharedDwelling.getOwner() == owner) {
						validConnectedRoads.add(r);
					}
				}
			}
		}

		return !validConnectedRoads.isEmpty();
	}

	/**
	 * Places a road at the given edge
	 *
	 * @param road
	 *            the road to be placed on the board
	 * @param location
	 *            the desired edge for the road
	 * @throws CatanException
	 *             if the road cannot be placed at the desired location
	 */
	public void placeRoad(Road road, EdgeLocation edge) throws CatanException {
		if (this.canPlaceRoad(road, edge)) {
			road.setLocation(edge);
			this.roads.put(edge, road);
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
	 */
	public boolean canPlaceDwelling(Dwelling dwelling, VertexLocation vertex) {
		// disallow if a dwelling already exists here
		if (this.dwellings.get(vertex) != null) {
			return false;
		}

		// disallow if this vertex is not on land
		if (!this.isLand(vertex)) {
			return false;
		}

		Collection<Dwelling> adjacentDwellings = this.getAdjacentDwellings(vertex);

		PlayerNumber owner = dwelling.getOwner();
		Collection<Road> connectedRoads = this.getAdjacentRoads(vertex);
		Collection<Road> validConnectedRoads = new ArrayList<Road>();
		for (Road road : connectedRoads) {
			if (road.getOwner() == owner) {
				validConnectedRoads.add(road);
			}
		}

		return adjacentDwellings.isEmpty() && !validConnectedRoads.isEmpty();
	}

	/**
	 * Places a dwelling at the given vertex
	 *
	 * @param dwelling
	 *            the dwelling to be placed on the board
	 * @param location
	 *            the desired vertex for the dwelling
	 * @throws CatanException
	 *             if the dwelling cannot be placed at the desired location
	 */
	public void placeDwelling(Dwelling dwelling, VertexLocation vertex) throws CatanException {
		if (this.canPlaceDwelling(dwelling, vertex)) {
			dwelling.setLocation(vertex);
			this.dwellings.put(vertex, dwelling);
		}
		else {
			String message = "A dwelling cannot be placed at " + vertex.toString();
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, message);
		}
	}
}
