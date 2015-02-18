package clientBackend.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import clientBackend.transport.TransportBank;
import clientBackend.transport.TransportDeck;
import clientBackend.transport.TransportPlayer;
import shared.definitions.*;

/**
 * Responsible for the transfer of cards between and among the bank and players'
 * holdings 
 * */
public class Broker {
	private Map<PlayerNumber, Hand> holdings;
	//private Bank bank;
	/**
	 * Default constructor for the broker.
	 * This will go through and make each one of the hands.
	 * Not implemented
	 */
	public Broker()
	{
		holdings = new HashMap<PlayerNumber, Hand>();
		holdings.put(PlayerNumber.BANK, new Bank());
		holdings.put(PlayerNumber.ONE, new PlayerHoldings());
		holdings.put(PlayerNumber.TWO, new PlayerHoldings());
		holdings.put(PlayerNumber.THREE, new PlayerHoldings());
		holdings.put(PlayerNumber.FOUR, new PlayerHoldings());
		
	}
	public Broker(TransportBank resources, 
			TransportDeck bankDevCard, 
			Collection<TransportPlayer> playerList,
			Map<PlayerNumber, Collection<Harbor>> harborMap)
	{
		holdings = new HashMap<PlayerNumber, Hand>();
		holdings.put(PlayerNumber.BANK, new Bank(bankDevCard, resources));
		for(TransportPlayer player: playerList)
		{
			PlayerNumber playerNum = player.playerIndex;
			holdings.put(playerNum, new PlayerHoldings(player,
														harborMap.get(playerNum)));
		}
	}
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
		int brick = invoice.getBrick();
		int wood = invoice.getWood();
		int wheat = invoice.getWheat();
		int sheep = invoice.getSheep();
		int ore = invoice.getOre();
		if(brick != 0){
			if(brick > 0){
				success = tradeCards(srcPlayer,dstPlayer,ResourceType.BRICK,brick);
			}
			else{
				success = tradeCards(dstPlayer,srcPlayer,ResourceType.BRICK,-brick);
			}
		}
		if(wood != 0){
			if(wood > 0){
				success = tradeCards(srcPlayer,dstPlayer,ResourceType.WOOD,wood);
			}
			else{
				success = tradeCards(dstPlayer,srcPlayer,ResourceType.WOOD,-wood);
			}
		}
		if(wheat != 0){
			if(wheat > 0){
				success = tradeCards(srcPlayer,dstPlayer,ResourceType.WHEAT,wheat);
			}
			else{
				success = tradeCards(dstPlayer,srcPlayer,ResourceType.WHEAT,-wheat);
			}
		}
		if(sheep != 0){
			if(sheep > 0){
				success = tradeCards(srcPlayer,dstPlayer,ResourceType.SHEEP,sheep);
			}
			else{
				success = tradeCards(dstPlayer,srcPlayer,ResourceType.SHEEP,-sheep);
			}
		}
		if(ore != 0){
			if(ore > 0){
				success = tradeCards(srcPlayer,dstPlayer,ResourceType.ORE,ore);
			}
			else{
				success = tradeCards(dstPlayer,srcPlayer,ResourceType.ORE,-ore);
			}
		}
		return success;
	}
	/**depriciated
	 * We dont need this because now process invoice does this all for us
	 * @param srcPlayer
	 * @param dstPlayer
	 * @param type
	 * @param count
	 * @return
	 * @throws CatanException
	 */
	private boolean tradeCards(Hand srcPlayer, Hand dstPlayer, ResourceType type, int count) throws CatanException{
		boolean success = false;
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
		ResourceInvoice purchase;
		if(!(this.canPurchase(player, type))){
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Not enough resource cards to purchase");
		}
		switch(type){
		case ROAD:
			purchase = new ResourceInvoice(player, PlayerNumber.BANK);
			purchase.setBrick(1);
			purchase.setWood(1);
			this.processInvoice(purchase);
			break;
		case CITY:
			purchase = new ResourceInvoice(player, PlayerNumber.BANK);
			purchase.setOre(3);
			purchase.setWheat(2);
			this.processInvoice(purchase);
			break;
		case SETTLEMENT:
			purchase = new ResourceInvoice(player, PlayerNumber.BANK);
			purchase.setBrick(1);
			purchase.setWood(1);
			purchase.setSheep(1);
			purchase.setWheat(1);
			this.processInvoice(purchase);
			break;
		case DEVELOPMENT_CARD:
			purchase = new ResourceInvoice(player, PlayerNumber.BANK);
			purchase.setOre(1);
			purchase.setSheep(1);
			purchase.setWheat(1);
			this.processInvoice(purchase);
			//if this happens draw a dev card.
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
		if(player == PlayerNumber.BANK){
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "The bank cannot play development cards.");
		}
		PlayerHoldings local = (PlayerHoldings) holdings.get(player);
		//check if the card is in the playabel collections
		//need to check the playable boolean
		if(local.getDevelopmentCardCount(type) >= 1)
		{
			for(DevelopmentCard card: local.getDevelopmentCards().get(type)){
				if(card.isPlayable()){
					success = true;
					return success;
				}
			}
			
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
	 * tells if a Given player can use a special maritime trade
	 * @param player
	 * @return return true if he can false otherwise
	 */
	//not sure what this should be
	//maritime trade can be any ratio 2,3,4 as long as they have the correct port.

	public boolean canMaritimeTrade(PlayerNumber player){
		boolean enough = false;
		for(ResourceType type: ResourceType.values())
		{
			if(holdings.get(player).getResourceCardCount(type) >= 4)
			{
				enough = true;
				return enough;
			}
		}
		return enough;
	}
	
	/**
	 * This is a method to check if the source player in the invoice
	 * has the necessary resources to trade.
	 * @param invoice
	 * @return true if can offer trade false otherwise
	 */
	public boolean canOfferTrade(ResourceInvoice invoice)
	{
		boolean canTrade = false;
		PlayerNumber player = invoice.getSourcePlayer();
		PlayerHoldings hand = (PlayerHoldings)holdings.get(player);
		if(hand.getResourceCardCount(ResourceType.BRICK)>=invoice.getBrick()){
			canTrade = true;
		}
		else{
			canTrade = false;
			return canTrade;
		}
		if(hand.getResourceCardCount(ResourceType.WHEAT)>=invoice.getWheat()){
			canTrade = true;
		}
		else{
			canTrade = false;
			return canTrade;
		}
		if(hand.getResourceCardCount(ResourceType.WOOD)>=invoice.getWood()){
			canTrade = true;
		}
		else{
			canTrade = false;
			return canTrade;
		}
		if(hand.getResourceCardCount(ResourceType.ORE)>=invoice.getOre()){
			canTrade = true;
		}
		else{
			canTrade = false;
			return canTrade;
		}
		if(hand.getResourceCardCount(ResourceType.SHEEP)>=invoice.getSheep()){
			canTrade = true;
		}
		else{
			canTrade = false;
			return canTrade;
		}
		return canTrade;//canDiscardCards(player, 1);
	}
	
	/**
	 * This method will check the destination player to see 
	 * if they have enough resources to complete the trade.
	 * @param invoice
	 * @return return true if the dest player has the needed resources false otherwise.
	 */
	public boolean canAcceptTrade(ResourceInvoice invoice){
		//TODO not makeing the correct comparison
		boolean canAccept = false;
		PlayerNumber player = invoice.getSourcePlayer();
		PlayerHoldings hand = (PlayerHoldings)holdings.get(player);
		if((-(hand.getResourceCardCount(ResourceType.BRICK)))<=invoice.getBrick()){
			canAccept = true;
		}
		else{
			canAccept = false;
			return canAccept;
		}
		if(-(hand.getResourceCardCount(ResourceType.WHEAT))<=invoice.getWheat()){
			canAccept = true;
		}
		else{
			canAccept = false;
			return canAccept;
		}
		if(-(hand.getResourceCardCount(ResourceType.WOOD))<=invoice.getWood()){
			canAccept = true;
		}
		else{
			canAccept = false;
			return canAccept;
		}
		if(-(hand.getResourceCardCount(ResourceType.ORE))<=invoice.getOre()){
			canAccept = true;
		}
		else{
			canAccept = false;
			return canAccept;
		}
		if(-(hand.getResourceCardCount(ResourceType.SHEEP))<=invoice.getSheep()){
			canAccept = true;
		}
		else{
			canAccept = false;
			return canAccept;
		}
		return canAccept;
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
	
	/**
	 * Determines whether the player has at least one resource card
	 * @param player
	 * @return
	 */
	//TODO not sure what this is meant to do?
	public boolean hasResourceCard(PlayerNumber player){
		boolean hasCard = false;
		Hand local = holdings.get(player);
		for(ResourceType type: ResourceType.values()){
			if(local.getResourceCardCount(type) > 0){
				hasCard = true;
				break;
			}
		}
		return hasCard;
	}
	
	/**
	 * Determines if a player has at least one harbor
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean hasHarbor(PlayerNumber player) throws CatanException {
		
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot call hasHarbor() on BANK");
		}
		
		PlayerHoldings playerHoldings = (PlayerHoldings) holdings.get(player);
		
		if (playerHoldings.getHarbors().size() > 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if a player has at least one development card
	 * @param player
	 * @return
	 */
	public boolean hasDevelopmentCard(PlayerNumber player) {
		
		if (holdings.get(player).getDevelopmentCardCount(null) > 0) {
			return true;
		}
		
		return false;
	}
	
	public void makeDevelopmentCardsPlayable(PlayerNumber player) throws CatanException {
		
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Cannot call hasHarbor() on BANK");
		}
		
		PlayerHoldings playerHoldings = (PlayerHoldings) holdings.get(player);
		
		playerHoldings.makeDevelopementCardsPlayable();
	}
	public Map<PlayerNumber, Hand> getHoldings() {
		return holdings;
	}
	//canOfferTrade(PlayerNumber player, PlayerNumber receiver, ResourceInvoice offer);
	//canMaritimeTrade(PlayerNumber player, int ratio, ResourceType giving, ResourceType getting);
}
