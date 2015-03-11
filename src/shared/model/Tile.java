package shared.model;

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

	public Tile(HexLocation location, ResourceType resourceType, boolean hasRobber) {
		this.location = location;
		this.resourceType = resourceType;
		this.hasRobber = hasRobber;
	}

	public HexLocation getLocation() {
		return this.location;
	}

	public void setLocation(HexLocation location) {
		this.location = location;
	}

	public ResourceType getResourceType() {
		return this.resourceType;
	}

	public boolean hasRobber() {
		return this.hasRobber;
	}

	public void setHasRobber(boolean hasRobber) {
		this.hasRobber = hasRobber;
	}

	/**
	 * Returns a collection of dwellings that are connected to the tile
	 *
	 * @return a collection of dwellings that are connected to the tile
	 */
	public Collection<Dwelling> getConnectedDwellings() {
		return null;
	}

	@Override
	public String toString() {
		return "Tile [location=" + this.location + ", resourceType=" + this.resourceType
				+ ", hasRobber="
				+ this.hasRobber + "]";
	}

}
