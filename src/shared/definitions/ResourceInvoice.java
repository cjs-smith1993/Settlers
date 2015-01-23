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
}
