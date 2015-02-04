package clientBackend.model;

import java.util.Collection;
import java.util.Map;

import shared.definitions.*;

/**
 * Responsible for the transfer of cards between and among the bank and players'
 * holdings 
 * */
public class Broker {
	private Map<PlayerNumber, Hand> holdings;
	//private Bank bank;
	
	/**
	 * Initiates a transfer of cards between or among the bank and players' 
	 * holdings
	 * @param invoice an invoices to process
	 * @return indicates success of invoices
	 * @throws CatanException if the invoice cannot be processed
	 * */
	public boolean processInvoice(ResourceInvoice invoice) throws CatanException {
		boolean success = false;
		Hand srcPlayer = holdings.get(invoice.getSourcePlayer());
		Hand dstPlayer = holdings.get(invoice.getDestinationPlayer());
		ResourceType type = invoice.getResource();
		int count = invoice.getCount();
		//add to the logic if bank do special things
		Collection<ResourceCard> lostResources = srcPlayer.removeResourceCard(type, count);
		if(lostResources.isEmpty()){
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "There was not enough cards in the hand to remove!");
		}
		success = dstPlayer.addResourceCardCollection(type, lostResources);
		if(!success){
			srcPlayer.addResourceCardCollection(type, lostResources);
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "The cards were not added to the destination player!");
		}
		
		return success;
	}
	
	/**
	 * Returns whether the player can purchase the desired property
	 * @param player the player
	 * @param type the desired property type
	 * @return true if the player can purchase the desired property
	 */
	public boolean canPurchase(PlayerNumber player, PropertyType type) throws CatanException{
		boolean purchasable = false;
		if(player == PlayerNumber.BANK)
		{
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "The bank cannot purchase resource cards"); 
		}
		Hand local = holdings.get(player);
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
				if(local.getResourceCardCount(ResourceType.SHEEP) >= 1 
				&& local.getResourceCardCount(ResourceType.ORE) >= 1 
				&& local.getResourceCardCount(ResourceType.WHEAT) >= 1)
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
		ResourceInvoice brick;
		ResourceInvoice wood;
		ResourceInvoice sheep;
		ResourceInvoice wheat;
		ResourceInvoice ore;
		if(!(this.canPurchase(player, type))){
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Not enough resource cards to purchase");
		}
		switch(type){
		case ROAD:
			brick = new ResourceInvoice(ResourceType.BRICK, 1, player, PlayerNumber.BANK);
			wood = new ResourceInvoice(ResourceType.WOOD, 1, player, PlayerNumber.BANK);
			this.processInvoice(brick);
			this.processInvoice(wood);
			break;
		case CITY:
			ore = new ResourceInvoice(ResourceType.ORE, 3, player, PlayerNumber.BANK);
			wheat = new ResourceInvoice(ResourceType.WHEAT, 2, player, PlayerNumber.BANK);
			this.processInvoice(ore);
			this.processInvoice(wheat);
			break;
		case SETTLEMENT:
			brick = new ResourceInvoice(ResourceType.BRICK, 1, player, PlayerNumber.BANK);
			wood = new ResourceInvoice(ResourceType.WOOD, 1, player, PlayerNumber.BANK);
			sheep = new ResourceInvoice(ResourceType.SHEEP, 1, player, PlayerNumber.BANK);
			wheat = new ResourceInvoice(ResourceType.WHEAT, 1, player, PlayerNumber.BANK);
			this.processInvoice(brick);
			this.processInvoice(wood);
			this.processInvoice(sheep);
			this.processInvoice(wheat);
			break;
		case DEVELOPMENT_CARD:
			ore = new ResourceInvoice(ResourceType.ORE, 1, player, PlayerNumber.BANK);
			sheep = new ResourceInvoice(ResourceType.SHEEP, 1, player, PlayerNumber.BANK);
			wheat = new ResourceInvoice(ResourceType.WHEAT, 1, player, PlayerNumber.BANK);
			this.processInvoice(ore);
			this.processInvoice(sheep);
			this.processInvoice(wheat);
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
			DevCardType type) throws CatanException {
		boolean success = false;
		if(player != PlayerNumber.BANK){
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "The bank cannot play development cards.");
		}
		PlayerHoldings local = (PlayerHoldings) holdings.get(player);
		//check if the card is in the playabel collections
		if(local.getDevelopmentCardCount(type) >= 1)
		{
			success = true;
		}
		return success;
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
		boolean beenPlayed = false;
		if(!(this.canPlayDevelopmentCard(player, type))){
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "The type of card is not in the player holdings");
		}
		Collection<DevelopmentCard> transDevCard = null;
		PlayerHoldings local = (PlayerHoldings) holdings.get(player);
		switch (type){
		case SOLDIER:
			//move card to the soldier played deck
			transDevCard = local.removeDevelopmentCard(type, 1);
			if(transDevCard.isEmpty()){
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "There were no Soldier cards to remove!");
			}
			beenPlayed = local.addDevelopmentCardCollection(type, transDevCard);
			if(!beenPlayed){
				local.addDevelopmentCardCollection(type, transDevCard);
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "The Soldiers were not stored in the bank!");
			}
			break;
		case MONUMENT:
			//move the card to the monumnet played deck
			transDevCard = local.removeDevelopmentCard(type, 1);
			if(transDevCard.isEmpty()){
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "There were no Moument cards to remove!");
			}
			beenPlayed = local.addDevelopmentCardCollection(type, transDevCard);
			if(!beenPlayed){
				local.addDevelopmentCardCollection(type, transDevCard);
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "The Monuments were not stored in the discard!");
			}
			break;
		default:
			//move the card to the bank played deck
			transDevCard = local.removeDevelopmentCard(type, 1);
			if(transDevCard.isEmpty()){
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "The cards were not removed!");
			}
			beenPlayed = holdings.get(PlayerNumber.BANK).addDevelopmentCardCollection(type, transDevCard);
			if(!beenPlayed){
				local.addDevelopmentCardCollection(type, transDevCard);
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "The cards were not stored in the bank!");
			}
			break;
		}
		
		return beenPlayed;
		
	}

	/**
	 * Determines whether the player has the necessary number of cards to return to the BANK.
	 * @return canDiscardCards
	 */
	public boolean canDiscardCards(PlayerNumber player, int discardAmount) {
		int cardCount = 0;
		
		cardCount += holdings.get(player).getResourceCardCount(ResourceType.BRICK);
		cardCount += holdings.get(player).getResourceCardCount(ResourceType.ORE);
		cardCount += holdings.get(player).getResourceCardCount(ResourceType.SHEEP);
		cardCount += holdings.get(player).getResourceCardCount(ResourceType.WHEAT);
		cardCount += holdings.get(player).getResourceCardCount(ResourceType.WOOD);
		
		if (cardCount >= discardAmount) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines whether the player has adequate resources.
	 * @param resourceType
	 * @param requiredQuantity
	 * @return hasNecessaryResource
	 */
	public boolean hasNecessaryResourceAmount(PlayerNumber player, ResourceType resourceType, int requiredQuantity) {
		if (holdings.get(player).getResourceCardCount(resourceType) >= requiredQuantity) {
			return true;
		}

		return false;
	}
}
