package clientBackend.model;

import java.util.*;

import clientBackend.transport.*;
import shared.definitions.*;
import shared.locations.*;

public class BoardFactory {
	public static Map<Integer, Collection<Chit>> generateChits(boolean randomize) {
		Map<Integer, Collection<Chit>> chits = new HashMap<Integer, Collection<Chit>>();

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

		ArrayList<HexLocation> locations = getLandHexLocations(randomize);

		int idx = 0; // only use the first 18 (the 19th is the Desert)
		Chit a = new Chit('A', 5, locations.get(idx++));
		Chit b = new Chit('B', 2, locations.get(idx++));
		Chit c = new Chit('C', 6, locations.get(idx++));
		Chit d = new Chit('D', 3, locations.get(idx++));
		Chit e = new Chit('E', 8, locations.get(idx++));
		Chit f = new Chit('F', 10, locations.get(idx++));
		Chit g = new Chit('G', 9, locations.get(idx++)); // or 6?
		Chit h = new Chit('H', 12, locations.get(idx++));
		Chit i = new Chit('I', 11, locations.get(idx++));
		Chit j = new Chit('J', 4, locations.get(idx++));
		Chit k = new Chit('K', 8, locations.get(idx++));
		Chit l = new Chit('L', 10, locations.get(idx++));
		Chit m = new Chit('M', 9, locations.get(idx++));
		Chit n = new Chit('N', 4, locations.get(idx++));
		Chit o = new Chit('O', 5, locations.get(idx++));
		Chit p = new Chit('P', 6, locations.get(idx++)); // or 9?
		Chit q = new Chit('Q', 3, locations.get(idx++));
		Chit r = new Chit('R', 11, locations.get(idx++));

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

		chits.put(2, twos);
		chits.put(3, threes);
		chits.put(4, fours);
		chits.put(5, fives);
		chits.put(6, sixes);
		chits.put(8, eights);
		chits.put(9, nines);
		chits.put(10, tens);
		chits.put(11, elevens);
		chits.put(12, twelves);

		return chits;
	}

	public static Map<HexLocation, Tile> generateTiles(boolean randomize) {
		Map<HexLocation, Tile> tiles = new HashMap<HexLocation, Tile>();

		ArrayList<HexLocation> landHexes = getLandHexLocations(randomize);
		ArrayList<Tile> landTiles = getLandTiles();
		int idx = 0;
		for (HexLocation location : landHexes) {
			Tile tile = landTiles.get(idx++);
			tile.setLocation(location);
			tiles.put(location, tile);
		}

		ArrayList<HexLocation> waterHexes = getWaterHexLocations(false);
		ArrayList<Tile> waterTiles = getWaterTiles();
		idx = 0;
		for (HexLocation location : waterHexes) {
			Tile tile = waterTiles.get(idx++);
			tile.setLocation(location);
			tiles.put(location, tile);
		}

		return tiles;
	}

	public static Collection<Harbor> generateHarbors(boolean randomize) {
		ArrayList<Harbor> harbors = getHarbors(randomize);

		ArrayList<EdgeLocation> ports = getPortLocations();
		int idx = 0;
		for (Harbor harbor : harbors) {
			// get the hex location
			EdgeLocation port = ports.get(idx++);
			HexLocation location = port.getHexLoc();
			harbor.setLocation(location);

			// get the two vertices
			EdgeLocation normalized = port.getNormalizedLocation();
			Collection<VertexLocation> currentPorts = Geometer.getAdjacentVertices(normalized);
			harbor.setPorts(currentPorts);
		}

		return harbors;
	}

	private static ArrayList<HexLocation> getLandHexLocations(boolean randomize) {
		ArrayList<HexLocation> landHexes = new ArrayList<HexLocation>();

		// hexes for chits
		landHexes.add(new HexLocation(-2, 0));	// A
		landHexes.add(new HexLocation(-2, 1));	// B
		landHexes.add(new HexLocation(-2, 2));	// C
		landHexes.add(new HexLocation(-1, 2));	// D
		landHexes.add(new HexLocation(0, 2));	// E
		landHexes.add(new HexLocation(1, 1));	// F
		landHexes.add(new HexLocation(2, 0));	// G
		landHexes.add(new HexLocation(2, -1));	// H
		landHexes.add(new HexLocation(2, -2));	// I
		landHexes.add(new HexLocation(1, -2));	// J
		landHexes.add(new HexLocation(-1, -1));	// K
		landHexes.add(new HexLocation(-1, 0));	// L
		landHexes.add(new HexLocation(-1, 1));	// M
		landHexes.add(new HexLocation(0, 1));	// N
		landHexes.add(new HexLocation(1, 0));	// O
		landHexes.add(new HexLocation(1, -1));	// P
		landHexes.add(new HexLocation(0, -1));	// Q
		landHexes.add(new HexLocation(0, 0)); 	// R

		// hex for Robber
		landHexes.add(new HexLocation(0, 2));

		if (randomize) {
			Collections.shuffle(landHexes);
		}

		return landHexes;
	}

	private static ArrayList<HexLocation> getWaterHexLocations(boolean randomize) {
		ArrayList<HexLocation> waterHexes = new ArrayList<HexLocation>();
		waterHexes.add(new HexLocation(0, 3));
		waterHexes.add(new HexLocation(1, 2));
		waterHexes.add(new HexLocation(2, 1));
		waterHexes.add(new HexLocation(3, 0));
		waterHexes.add(new HexLocation(3, -1));
		waterHexes.add(new HexLocation(3, -2));
		waterHexes.add(new HexLocation(3, -3));
		waterHexes.add(new HexLocation(2, -3));
		waterHexes.add(new HexLocation(1, -3));
		waterHexes.add(new HexLocation(0, -3));
		waterHexes.add(new HexLocation(-1, -2));
		waterHexes.add(new HexLocation(-2, -1));
		waterHexes.add(new HexLocation(-3, 0));
		waterHexes.add(new HexLocation(-3, 1));
		waterHexes.add(new HexLocation(-3, 2));
		waterHexes.add(new HexLocation(-3, 3));
		waterHexes.add(new HexLocation(-2, 3));
		waterHexes.add(new HexLocation(-1, 3));

		if (randomize) {
			Collections.shuffle(waterHexes);
		}

		return waterHexes;
	}

	private static ArrayList<Tile> getLandTiles() {
		ArrayList<Tile> landTiles = new ArrayList<Tile>();

		// tiles for chits
		landTiles.add(new Tile(null, ResourceType.ORE, false));		// A
		landTiles.add(new Tile(null, ResourceType.WHEAT, false));	// B
		landTiles.add(new Tile(null, ResourceType.WOOD, false));	// C
		landTiles.add(new Tile(null, ResourceType.ORE, false));		// D
		landTiles.add(new Tile(null, ResourceType.WHEAT, false));	// E
		landTiles.add(new Tile(null, ResourceType.SHEEP, false));	// F
		landTiles.add(new Tile(null, ResourceType.WHEAT, false));	// G
		landTiles.add(new Tile(null, ResourceType.SHEEP, false));	// H
		landTiles.add(new Tile(null, ResourceType.WOOD, false));	// I
		landTiles.add(new Tile(null, ResourceType.BRICK, false));	// J
		landTiles.add(new Tile(null, ResourceType.BRICK, false));	// K
		landTiles.add(new Tile(null, ResourceType.SHEEP, false));	// L
		landTiles.add(new Tile(null, ResourceType.SHEEP, false));	// M
		landTiles.add(new Tile(null, ResourceType.WOOD, false));	// N
		landTiles.add(new Tile(null, ResourceType.BRICK, false));	// O
		landTiles.add(new Tile(null, ResourceType.ORE, false));		// P
		landTiles.add(new Tile(null, ResourceType.WOOD, false));	// Q
		landTiles.add(new Tile(null, ResourceType.WHEAT, false));	// R

		// tile for Desert
		landTiles.add(new Tile(null, ResourceType.NONE, true));

		return landTiles;
	}

	private static ArrayList<Tile> getWaterTiles() {
		ArrayList<Tile> waterTiles = new ArrayList<Tile>();

		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));
		waterTiles.add(new Tile(null, ResourceType.ALL, false));

		return waterTiles;
	}

	private static ArrayList<Harbor> getHarbors(boolean randomize) {
		ArrayList<Harbor> harbors = new ArrayList<Harbor>();

		harbors.add(new Harbor(null, null, ResourceType.ALL, 3));
		harbors.add(new Harbor(null, null, ResourceType.ALL, 3));
		harbors.add(new Harbor(null, null, ResourceType.SHEEP, 2));
		harbors.add(new Harbor(null, null, ResourceType.ALL, 3));
		harbors.add(new Harbor(null, null, ResourceType.ORE, 2));
		harbors.add(new Harbor(null, null, ResourceType.WHEAT, 2));
		harbors.add(new Harbor(null, null, ResourceType.ALL, 3));
		harbors.add(new Harbor(null, null, ResourceType.WOOD, 2));
		harbors.add(new Harbor(null, null, ResourceType.BRICK, 2));

		if (randomize) {
			Collections.shuffle(harbors);
		}

		return harbors;
	}

	private static ArrayList<EdgeLocation> getPortLocations() {
		ArrayList<EdgeLocation> ports = new ArrayList<EdgeLocation>();

		ports.add(new EdgeLocation(new HexLocation(0, 3), EdgeDirection.North));
		ports.add(new EdgeLocation(new HexLocation(2, 1), EdgeDirection.NorthWest));
		ports.add(new EdgeLocation(new HexLocation(3, -1), EdgeDirection.NorthWest));
		ports.add(new EdgeLocation(new HexLocation(3, -3), EdgeDirection.SouthWest));
		ports.add(new EdgeLocation(new HexLocation(1, -3), EdgeDirection.South));
		ports.add(new EdgeLocation(new HexLocation(-1, -2), EdgeDirection.South));
		ports.add(new EdgeLocation(new HexLocation(-3, 0), EdgeDirection.SouthEast));
		ports.add(new EdgeLocation(new HexLocation(-3, 2), EdgeDirection.NorthEast));
		ports.add(new EdgeLocation(new HexLocation(-2, 3), EdgeDirection.NorthEast));

		return ports;
	}

	public static Map<Integer, Collection<Chit>> parseChits(List<TransportHex> t_hexes) {
		Map<Integer, Collection<Chit>> chits = new HashMap<Integer, Collection<Chit>>();

		for (TransportHex hex : t_hexes) {
			HexLocation location = new HexLocation(hex.location.x, hex.location.y);
			int number = hex.number;
			if (number > 0) {
				Chit chit = new Chit('?', number, location);
				Collection<Chit> collection = chits.get(number);

				if (collection == null) {
					collection = new ArrayList<Chit>();
				}
				collection.add(chit);

				chits.put(number, collection);
			}
		}

		return chits;
	}

	public static Map<HexLocation, Tile> parseTiles(List<TransportHex> t_hexes,
			TransportRobber t_robber, int radius) {
		Map<HexLocation, Tile> tiles = new HashMap<HexLocation, Tile>();

		// initialize everything to water
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				int z = x + y;
				if (Math.abs(z) > radius) {
					continue;
				}
				HexLocation location = new HexLocation(x, y);
				Tile tile = new Tile(location, ResourceType.ALL, false);
				tiles.put(location, tile);
			}
		}

		HexLocation robberLocation = new HexLocation(t_robber.x, t_robber.y);

		// parse the hexes
		for (TransportHex hex : t_hexes) {
			HexLocation location = new HexLocation(hex.location.x, hex.location.y);
			ResourceType type = hex.resource;
			if (type == null) {
				type = ResourceType.NONE;
			}
			boolean hasRobber = location.equals(robberLocation);
			Tile tile = new Tile(location, type, hasRobber);
			tiles.put(location, tile);
		}

		return tiles;
	}

	public static Map<EdgeLocation, Road> parseRoads(List<TransportRoad> t_roads) {
		Map<EdgeLocation, Road> roads = new HashMap<EdgeLocation, Road>();

		for (TransportRoad t_road : t_roads) {
			PlayerNumber owner = t_road.owner;
			int x = t_road.location.x;
			int y = t_road.location.y;
			EdgeDirection dir = t_road.location.direction;
			EdgeLocation location = new EdgeLocation(new HexLocation(x, y), dir);
			location = location.getNormalizedLocation();

			Road road = new Road(owner, location);
			roads.put(location, road);
		}

		return roads;
	};

	public static Map<VertexLocation, Dwelling> parseSettlements(
			List<TransportSettlement> t_settlements) {
		Map<VertexLocation, Dwelling> settlements = new HashMap<VertexLocation, Dwelling>();

		// parse the settlements
		for (TransportSettlement t_settlement : t_settlements) {
			PlayerNumber owner = t_settlement.owner;
			int x = t_settlement.location.x;
			int y = t_settlement.location.y;
			VertexDirection dir = t_settlement.location.direction;
			VertexLocation location = new VertexLocation(new HexLocation(x, y), dir);
			location = location.getNormalizedLocation();

			Settlement settlement = new Settlement(owner, location);
			settlements.put(location, settlement);
		}

		return settlements;
	}

	public static Map<VertexLocation, Dwelling> parseCities(List<TransportCity> t_cities) {
		Map<VertexLocation, Dwelling> cities = new HashMap<VertexLocation, Dwelling>();

		// parse the cities
		for (TransportCity t_city : t_cities) {
			PlayerNumber owner = t_city.owner;
			int x = t_city.location.x;
			int y = t_city.location.y;
			VertexDirection dir = t_city.location.direction;
			VertexLocation location = new VertexLocation(new HexLocation(x, y), dir);
			location = location.getNormalizedLocation();

			City city = new City(owner, location);
			cities.put(location, city);
		}

		return cities;
	}

	public static Collection<Harbor> parseHarbors(List<TransportPort> t_ports) {
		Collection<Harbor> harbors = new ArrayList<Harbor>();

		for (TransportPort t_port : t_ports) {
			int x = t_port.location.x;
			int y = t_port.location.y;
			HexLocation hexLoc = new HexLocation(x, y);
			EdgeDirection dir = t_port.direction;
			EdgeLocation edgeLoc = new EdgeLocation(hexLoc, dir);
			edgeLoc = edgeLoc.getNormalizedLocation();
			Collection<VertexLocation> ports = Geometer.getAdjacentVertices(edgeLoc);
			ResourceType type = t_port.resource;
			int ratio = t_port.ratio;

			Harbor harbor = new Harbor(hexLoc, ports, type, ratio);
			harbors.add(harbor);
		}

		return harbors;
	}

	public static Robber parseRobber(TransportRobber t_robber) {
		HexLocation location = new HexLocation(t_robber.x, t_robber.y);
		Robber robber = new Robber(location);
		return robber;
	}

}
