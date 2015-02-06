package clientBackend.model;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

/**
 * Data transfer object that encapsulates a one-sided transaction consisting of
 * one resource between one player and either another player or the bank
 */
public class ResourceInvoice {

	/**
	 * The type of the resource
	 */
	public ResourceType resource;

	/**
	 * The number of resources (greater than zero)
	 */
	public int count;

	/**
	 * The player from which the resource is being withdrawn, or the bank
	 */
	public PlayerNumber sourcePlayer;

	/**
	 * The player that is receiving the resource, or the bank
	 */
	public PlayerNumber destinationPlayer;

	public ResourceInvoice(ResourceType type, int count, PlayerNumber sourcePlayer,
			PlayerNumber destinationPlayer)
	{
		this.resource = type;
		this.count = count;
		this.sourcePlayer = sourcePlayer;
		this.destinationPlayer = destinationPlayer;
	}

	public ResourceType getResource() {
		return this.resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public PlayerNumber getSourcePlayer() {
		return this.sourcePlayer;
	}

	public void setSourcePlayer(PlayerNumber sourcePlayer) {
		this.sourcePlayer = sourcePlayer;
	}

	public PlayerNumber getDestinationPlayer() {
		return this.destinationPlayer;
	}

	public void setDestinationPlayer(PlayerNumber destinationPlayer) {
		this.destinationPlayer = destinationPlayer;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ResourceInvoice other = (ResourceInvoice) obj;
		if (this.count != other.count) {
			return false;
		}
		if (this.destinationPlayer != other.destinationPlayer) {
			return false;
		}
		if (this.resource != other.resource) {
			return false;
		}
		if (this.sourcePlayer != other.sourcePlayer) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ResourceInvoice [resource=" + this.resource + ", count=" + this.count
				+ ", sourcePlayer=" + this.sourcePlayer + ", destinationPlayer="
				+ this.destinationPlayer + "]";
	}

}
