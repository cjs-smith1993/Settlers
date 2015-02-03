package clientBackend.model;

import shared.definitions.PlayerNumber;
import shared.locations.VertexLocation;

/**
 * A settlement is a dwelling that adds one victory point to its owner, and that
 * generates one resource per connected tile per harvest
 */
public class Settlement extends Dwelling {

	public Settlement(PlayerNumber owner) {
		this.owner = owner;
		this.location = null;
		this.victoryPoints = 1;
	}

	public Settlement(PlayerNumber owner, VertexLocation location) {
		this.owner = owner;
		this.location = location;
		this.victoryPoints = 1;
	}
}
