package clientBackend.model;

import shared.definitions.PlayerNumber;
import shared.locations.VertexLocation;

/**
 * A city is a dwelling that adds two victory points to its owner, and that
 * generates two resources per connected tile per harvest
 */
public class City extends Dwelling {

	public City(PlayerNumber owner) {
		this.owner = owner;
		this.location = null;
		this.victoryPoints = 2;
	}

	public City(PlayerNumber owner, VertexLocation location) {
		this.owner = owner;
		this.location = location;
		this.victoryPoints = 2;
	}
}
