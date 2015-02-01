package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents the abstract concept of a dwelling. A dwelling adds a number of
 * victory points to the owner, depending on the dwelling's type. A dwelling is
 * located at a vertex and generates resources for the owner of the dwelling,
 * based off the types of the dwelling's adjacent tiles. The number of
 * resources a dwelling generates from a connected tile is equal to the number
 * of victory points for the dwelling
 */
public abstract class Dwelling {
	private VertexLocation location;
	protected int victoryPoints;
	private PlayerNumber owner;
	
	private Collection<Tile> getAdjacentTiles() {
		return null;
	}
	
	/**
	 * Generates and returns a collection of invoices. One invoice will be
	 * generated for each connected tile. Invoices with the same resource type
	 * will be combined into a single invoice
	 * @return a collection of invoices generated from each connected tile
	 */
	public Collection<ResourceInvoice> harvestResources() {
		return null;
	}

	public VertexLocation getLocation() {
		return this.location;
	}

	public void setLocation(VertexLocation location) {
		this.location = location;
	}

	public int getVictoryPoints() {
		return this.victoryPoints;
	}

	public PlayerNumber getOwner() {
		return this.owner;
	}
}
