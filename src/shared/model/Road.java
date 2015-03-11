package shared.model;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents a road. The owner of the road may build a dwelling on a vertex
 * that is connected to the road
 */
public class Road {
	private PlayerNumber owner;
	private EdgeLocation location;

	public Road(PlayerNumber owner) {
		this.owner = owner;
		this.location = null;
	}

	public Road(PlayerNumber owner, EdgeLocation location) {
		this.owner = owner;
		this.location = location;
	}

	public PlayerNumber getOwner() {
		return this.owner;
	}

	public EdgeLocation getLocation() {
		return this.location;
	}

	public void setLocation(EdgeLocation location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Road [owner=" + this.owner + ", location=" + this.location + "]";
	}
}
