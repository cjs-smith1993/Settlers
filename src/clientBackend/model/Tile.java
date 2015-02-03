package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents a tile on the game board. Each tile has a type that generates a
 * corresponding resource for all connected dwellings, except for the desert,
 * which has a resource type of NONE. If the robber is located on a tile, that
 * tile does not produce any resources for any of its connected dwellings
 */
public class Tile {
	private HexLocation location;
	private ResourceType resourceType;
	private boolean hasRobber;
	
	public HexLocation getLocation() {
		return this.location;
	}

	public ResourceType getResourceType() {
		return this.resourceType;
	}

	public boolean hasRobber() {
		return this.hasRobber;
	}
	
	/**
	 * Returns a collection of dwellings that are connected to the tile
	 * @return a collection of dwellings that are connected to the tile
	 */
	public Collection<Dwelling> getConnectedDwellings() {
		return null;
	}

}