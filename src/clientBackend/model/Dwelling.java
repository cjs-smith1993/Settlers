package clientBackend.model;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents the abstract concept of a dwelling. A dwelling adds a number of
 * victory points to the owner, depending on the dwelling's type. A dwelling is
 * located at a vertex and generates resources for the owner of the dwelling,
 * based off the types of the dwelling's adjacent tiles. The number of resources
 * a dwelling generates from a connected tile is equal to the number of victory
 * points for the dwelling
 */
public abstract class Dwelling {
	protected PlayerNumber owner;
	protected VertexLocation location;
	protected int victoryPoints;
	protected PropertyType propertyType;

 	public PlayerNumber getOwner() {
		return this.owner;
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
	
	public PropertyType getPropertyType() {
		return propertyType;
	}

	@Override
	public String toString() {
		return "Dwelling [owner=" + this.owner + ", victoryPoints=" + this.victoryPoints
				+ ", location=" + this.location + "]";
	}
}
