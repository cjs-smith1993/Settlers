package clientBackend.model;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

/**
 * Data transfer object that encapsulates a one-sided transaction consisting of
 * one resource between one player and either another player or the bank
 */
public class ResourceInvoice {

	/**
	 * number of brick to trade 
	 * (+) is the number to give to the dstPlayer
	 * (-) is the number to receive from the dstPlayer
	 */
	private int brick;
	/**
	 * number of wood to trade 
	 * (+) is the number to give to the dstPlayer
	 * (-) is the number to receive from the dstPlayer
	 */
	private int wood;
	/**
	 * number of wheat to trade 
	 * (+) is the number to give to the dstPlayer
	 * (-) is the number to receive from the dstPlayer
	 */
	private int wheat;
	/**
	 * number of ore to trade 
	 * (+) is the number to give to the dstPlayer
	 * (-) is the number to receive from the dstPlayer
	 */
	private int ore;
	/**
	 * number of sheep to trade 
	 * (+) is the number to give to the dstPlayer
	 * (-) is the number to receive from the dstPlayer
	 */
	private int sheep;

	/**
	 * The player from which the resource is being withdrawn, or the bank
	 */
	public PlayerNumber sourcePlayer;

	/**
	 * The player that is receiving the resource, or the bank
	 */
	public PlayerNumber destinationPlayer;

	public ResourceInvoice(PlayerNumber sourcePlayer,
			PlayerNumber destinationPlayer)
	{
		this.sourcePlayer = sourcePlayer;
		this.destinationPlayer = destinationPlayer;
		this.setBrick(0);
		this.setOre(0);
		this.setSheep(0);
		this.setWheat(0);
		this.setWood(0);
	}

	public int getBrick() {
		return brick;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getWheat() {
		return wheat;
	}

	public void setWheat(int wheat) {
		this.wheat = wheat;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getSheep() {
		return sheep;
	}

	public void setSheep(int sheep) {
		this.sheep = sheep;
	}

	public PlayerNumber getSourcePlayer() {
		return sourcePlayer;
	}

	public void setSourcePlayer(PlayerNumber sourcePlayer) {
		this.sourcePlayer = sourcePlayer;
	}

	public PlayerNumber getDestinationPlayer() {
		return destinationPlayer;
	}

	public void setDestinationPlayer(PlayerNumber destinationPlayer) {
		this.destinationPlayer = destinationPlayer;
	}

	@Override
	public String toString() {
		return "ResourceInvoice [brick=" + brick + ", wood=" + wood
				+ ", wheat=" + wheat + ", ore=" + ore + ", sheep=" + sheep
				+ ", sourcePlayer=" + sourcePlayer + ", destinationPlayer="
				+ destinationPlayer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brick;
		result = prime
				* result
				+ ((destinationPlayer == null) ? 0 : destinationPlayer
						.hashCode());
		result = prime * result + ore;
		result = prime * result + sheep;
		result = prime * result
				+ ((sourcePlayer == null) ? 0 : sourcePlayer.hashCode());
		result = prime * result + wheat;
		result = prime * result + wood;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceInvoice other = (ResourceInvoice) obj;
		if (brick != other.brick)
			return false;
		if (destinationPlayer != other.destinationPlayer)
			return false;
		if (ore != other.ore)
			return false;
		if (sheep != other.sheep)
			return false;
		if (sourcePlayer != other.sourcePlayer)
			return false;
		if (wheat != other.wheat)
			return false;
		if (wood != other.wood)
			return false;
		return true;
	}

}
