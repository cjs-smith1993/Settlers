package clientBackend.model;

import java.util.Collection;
import java.util.Map;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents the physical board, including all tiles, chits, harbors, roads, dwellings, and the robber
 */
public class Board {
	private Collection<Tile> tiles;
	private Collection<Harbor> harbors;
	private Map<Integer, Collection<Chit>> chits;
	private PlayerNumber longestRoad;
	private Robber robber;
	private Map<EdgeLocation, Road> roads;
	private Map<VertexLocation, Dwelling> dwellings;
	
	private Collection<Road> getAdjacentRoads(VertexLocation location) {
		return null;
	}

	private Collection<Road> getAdjacentRoads(EdgeLocation location) {
		return null;
	}
	
	private Collection<Dwelling> getAdjacentDwellings(EdgeLocation location) {
		return null;
	}
	
	/**
	 * Moves the robber to the given vertex TODO
	 * @param location the desired new location of the robber
	 */
	public void moveRobber(VertexLocation location) {
		
	}
	
	/**
	 * Generates and returns a collection of invoices corresponding to a given
	 * dice roll. One invoice will be generated for each resource type with a 
	 * non-zero count for each player.
	 * @param number the result of the dice roll
	 * @return a collection of invoices consisting of one invoice for each
	 * resource type with a non-zero count for each player
	 */
	public Collection<ResourceInvoice> generateInvoices(int number) {
		return null;
	}
	
	/**
	 * Places a road at the given edge TODO
	 * @param road the road to be placed on the board
	 * @param location the desired edge for the road
	 */
	public void placeRoad(Road road, EdgeLocation location) {
		
	}
	
	/**
	 * Places a dwelling at the given vertex TODO
	 * @param dwelling the dwelling to be placed on the board
	 * @param location the desired vertex for the dwelling
	 */
	public void placeDwelling(Dwelling dwelling, VertexLocation location) {
		
	}
}
