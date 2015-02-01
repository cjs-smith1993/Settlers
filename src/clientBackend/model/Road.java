package clientBackend.model;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents a road. The owner of the road may build a dwelling on a vertex
 * that is connected to the road
 */
public class Road {
	private EdgeLocation location;
	private PlayerNumber owner;
	
	public Road(PlayerNumber owner) {
		this.location = null;
		this.owner = owner;
	}
	
	public EdgeLocation getLocation() {
		return this.location;
	}
	
	public void setLocation(EdgeLocation location) {
		this.location = location;
	}
	
	public PlayerNumber getOwner() {
		return this.owner;
	}
}
