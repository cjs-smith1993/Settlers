package clientBackend.model;

import clientBackend.transport.TransportTradeOffer;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

/**
 * Data transfer object that encapsulates a one-sided transaction consisting of
 * one resource between one player and either another player or the bank
 */
public class ResourceInvoice {

	/**
	 * number of brick to trade (+) is the number to give to the dstPlayer (-)
	 * is the number to receive from the dstPlayer
	 */
	private int brick;
	/**
	 * number of wood to trade (+) is the number to give to the dstPlayer (-) is
	 * the number to receive from the dstPlayer
	 */
	private int wood;
	/**
	 * number of wheat to trade (+) is the number to give to the dstPlayer (-)
	 * is the number to receive from the dstPlayer
	 */
	private int wheat;
	/**
	 * number of ore to trade (+) is the number to give to the dstPlayer (-) is
	 * the number to receive from the dstPlayer
	 */
	private int ore;
	/**
	 * number of sheep to trade (+) is the number to give to the dstPlayer (-)
	 * is the number to receive from the dstPlayer
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
	
	public ResourceInvoice(TransportTradeOffer offer) {
		this.sourcePlayer = offer.sender;
		this.destinationPlayer = offer.receiver;
		this.setBrick(offer.offer.brick);
		this.setOre(offer.offer.ore);
		this.setSheep(offer.offer.sheep);
		this.setWheat(offer.offer.wheat);
		this.setWood(offer.offer.wood);
	}

	public int getBrick() {
		return this.brick;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	public int getWood() {
		return this.wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getWheat() {
		return this.wheat;
	}

	public void setWheat(int wheat) {
		this.wheat = wheat;
	}

	public int getOre() {
		return this.ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getSheep() {
		return this.sheep;
	}

	public void setSheep(int sheep) {
		this.sheep = sheep;
	}

	public int getResource(ResourceType type) {
		switch (type) {
		case WOOD:
			return this.getWood();
		case BRICK:
			return this.getBrick();
		case SHEEP:
			return this.getSheep();
		case WHEAT:
			return this.getWheat();
		case ORE:
			return this.getOre();
		default:
			return 0;
		}
	}

	public void setResource(ResourceType type, int count) {
		switch (type) {
		case WOOD:
			this.setWood(count);
			break;
		case BRICK:
			this.setBrick(count);
			break;
		case SHEEP:
			this.setSheep(count);
			break;
		case WHEAT:
			this.setWheat(count);
			break;
		case ORE:
			this.setOre(count);
			break;
		default:
		}
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
	public String toString() {
		return "ResourceInvoice [brick=" + this.brick + ", wood=" + this.wood
				+ ", wheat=" + this.wheat + ", ore=" + this.ore + ", sheep=" + this.sheep
				+ ", sourcePlayer=" + this.sourcePlayer + ", destinationPlayer="
				+ this.destinationPlayer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.brick;
		result = prime
				* result
				+ ((this.destinationPlayer == null) ? 0 : this.destinationPlayer
						.hashCode());
		result = prime * result + this.ore;
		result = prime * result + this.sheep;
		result = prime * result
				+ ((this.sourcePlayer == null) ? 0 : this.sourcePlayer.hashCode());
		result = prime * result + this.wheat;
		result = prime * result + this.wood;
		return result;
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
		if (this.brick != other.brick) {
			return false;
		}
		if (this.destinationPlayer != other.destinationPlayer) {
			return false;
		}
		if (this.ore != other.ore) {
			return false;
		}
		if (this.sheep != other.sheep) {
			return false;
		}
		if (this.sourcePlayer != other.sourcePlayer) {
			return false;
		}
		if (this.wheat != other.wheat) {
			return false;
		}
		if (this.wood != other.wood) {
			return false;
		}
		return true;
	}

}
