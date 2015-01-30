package clientBackend.model;

import java.util.List;
import java.util.Map;

import shared.definitions.*;

/**
 * Responsible for the transfer of cards between and among the bank and players'
 * holdings 
 * */
public class Broker {
	private Map<PlayerNumber, PlayerHoldings> holdings;
	
	/**
	 * Initiates a transfer of cards between or among the bank and players' 
	 * holdings
	 * @param invoice an invoices to process
	 * @return indicates success of invoices
	 * @throws CatanException if the invoice cannot be processed
	 * */
	public boolean processInvoice(ResourceInvoice invoice) throws CatanException {
		throw new CatanException();
	}
	
	/**
	 * Returns whether the player can purchase the desired property
	 * @param player the player
	 * @param type the desired property type
	 * @return true if the player can purchase the desired property
	 */
	public boolean canPurchase(PlayerNumber player, PropertyType type) {
		boolean purchasable = false;
		PlayerHoldings local = holdings.get(player);
		switch (type) {
			case ROAD:
				if(local.getResourceCardCount(ResourceType.BRICK) >= 1 
				&& local.getResourceCardCount(ResourceType.WOOD) >= 1)
				{
					purchasable = true;
				}
				break;
			case CITY:
				if(local.getResourceCardCount(ResourceType.ORE) >= 3 
				&& local.getResourceCardCount(ResourceType.WHEAT) >= 2)
				{
					purchasable = true;
				}
				break;
			case SETTLEMENT:
				if(local.getResourceCardCount(ResourceType.BRICK) >= 1 
				&& local.getResourceCardCount(ResourceType.WOOD) >= 1 
				&& local.getResourceCardCount(ResourceType.WHEAT) >= 1 
				&& local.getResourceCardCount(ResourceType.SHEEP) >= 1 )
				{
					purchasable = true;
				}
				break;
			case DEVELOPMENT_CARD:
				if(local.getResourceCardCount(ResourceType.SHEEP) >= 1 && 
					local.getResourceCardCount(ResourceType.ORE) >= 1 &&
					local.getResourceCardCount(ResourceType.WHEAT) >= 1)
				{
					purchasable = true;
				}
				break;
		}
		return purchasable;
	}
	
	/**
	 * Transfers the correct cards from a player's holdings to the bank
	 * for the given property
	 * @param player the number of the player making the purchase
	 * @param propertyType the type of property the player is purchasing
	 * @return indicates success of purchase
	 * @throws CatanException if the player cannot purchase the property
	 */
	public void purchase(PlayerNumber player, PropertyType type) throws CatanException {
		if(!(this.canPurchase(player, type)))
		{
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Not enough resource cards to purchase");
		}
		switch(type)
		{
		case ROAD:
			
			break;
		case CITY:
			
			break;
		case SETTLEMENT:
			
			break;
		case DEVELOPMENT_CARD:
			
			break;
		}
	}
	
	/**
	 * Returns whether the player can play the desired development card
	 * @param player the player
	 * @param type the desired development card type
	 * @return true if the player can play the desired development card
	 */
	public boolean canPlayDevelopmentCard(
			PlayerNumber player,
			DevCardType type) {
		return false;
	}
	
	/**
	 * Exchanges resource cards from a player's holdings for a random 
	 * development card from the bank
	 * @param player the number of the player playing the development card
	 * @param type the type of the development card being played
	 * @return indicates success of play
	 * @throws CatanException if the player cannot play the desired development
	 * card
	 */
	public boolean playDevelopmentCard(
			PlayerNumber player, 
			DevCardType type) throws CatanException {
		throw new CatanException();
	}

}
