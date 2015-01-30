package shared.definitions;

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
	
	public ResourceInvoice(ResourceType type, int count, PlayerNumber sourcePlayer, PlayerNumber destinationPlayer)
	{
		this.resource = type;
		this.count = count;
		this.sourcePlayer = sourcePlayer;
		this.destinationPlayer = destinationPlayer;
	}

	public ResourceType getResource() {
		return resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
	
}
