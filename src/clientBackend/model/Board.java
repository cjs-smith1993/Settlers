package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents the physical board, including all tiles, chits, harbors, roads,
 * dwellings, and the robber
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
	 * Returns whether the robber can be moved to the desired location
	 * @param location the desired location
	 * @return true if the robber can be moved to the desired location
	 */
	public boolean canMoveRobber(Tile newLocation) {
		Tile currentLocation = this.robber.getLocation();
		return currentLocation != newLocation;
	}

	/**
	 * Moves the robber to the given vertex
	 * @param location the desired new location of the robber
	 * @throws CatanException if the robber cannot be moved to the desired
	 * location
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
	 * @param number the result of the dice roll
	 * @return a collection of invoices consisting of one invoice for each
	 * resource type with a non-zero count for each player
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
	 * @param road the player's road
	 * @param location the desired location
	 * @return true if the road can be built at the desired location
	 */
	public boolean canPlaceRoad(Road road, EdgeLocation location) {
		return false;
	}

	/**
	 * Places a road at the given edge
	 * @param road the road to be placed on the board
	 * @param location the desired edge for the road
	 * @throws CatanException if the road cannot be placed at the desired
	 * location
	 */
	public void placeRoad(Road road, EdgeLocation location) throws CatanException {
		if (this.canPlaceRoad(road, location)) {

		}
		else {
			String message = "A road cannot be placed at " + location.toString();
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, message);
		}
	}

	/**
	 * Returns whether a dwelling can be placed at the desired location
	 * @param dwelling the player's dwelling
	 * @param location the desired location
	 */
	public boolean canPlaceDwelling(Dwelling dwelling, VertexLocation location) {
		return false;
	}

	/**
	 * Places a dwelling at the given vertex
	 * @param dwelling the dwelling to be placed on the board
	 * @param location the desired vertex for the dwelling
	 * @throws CatanException if the dwelling cannot be placed at the desired
	 * location
	 */
	public void placeDwelling(Dwelling dwelling, VertexLocation location) throws CatanException {
		if (this.canPlaceDwelling(dwelling, location)) {

		}
		else {
			String message = "A dwelling cannot be placed at " + location.toString();
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, message);
		}
	}
}
