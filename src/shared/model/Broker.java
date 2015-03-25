package shared.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import shared.definitions.*;
import shared.transport.TransportBank;
import shared.transport.TransportDeck;
import shared.transport.TransportPlayer;
import shared.transport.TransportTradeOffer;

/**
 * Responsible for the transfer of cards between and among the bank and players'
 * holdings
 * */
public class Broker {
	private Map<PlayerNumber, Hand> holdings;
	private ResourceInvoice tradeOffer = null; // Use this to manage trade offers as they come through the Broker.
	
	/**
	 * Default constructor for the broker. This will go through and make each
	 * one of the hands. Not implemented
	 */
	public Broker()
	{
		this.holdings = new HashMap<PlayerNumber, Hand>();
		this.holdings.put(PlayerNumber.BANK, new Bank());
		this.holdings.put(PlayerNumber.ONE, new PlayerHoldings());
		this.holdings.put(PlayerNumber.TWO, new PlayerHoldings());
		this.holdings.put(PlayerNumber.THREE, new PlayerHoldings());
		this.holdings.put(PlayerNumber.FOUR, new PlayerHoldings());
	}

	public Broker(
			TransportBank resources,
			TransportDeck bankDevCard,
			Collection<TransportPlayer> playerList,
			Map<PlayerNumber,
			Collection<Harbor>> harborMap) {
		this.holdings = new HashMap<PlayerNumber, Hand>();
		this.holdings.put(PlayerNumber.BANK, new Bank(bankDevCard, resources));
		
		for (TransportPlayer player : playerList) {
			if (player == null) {
				continue;
			}
			PlayerNumber playerNum = player.playerIndex;
			this.holdings.put(playerNum, new PlayerHoldings(player, harborMap.get(playerNum)));
		}
	}
	
	public TransportPlayer getTransportPlayer(TransportPlayer transportPlayer,PlayerNumber playerNumber) {
		return ((PlayerHoldings)holdings.get(playerNumber)).getTransportPlayer(transportPlayer);
	}
	
	public TransportBank getTransportBank() {
		return ((Bank)holdings.get(PlayerNumber.BANK)).getTransportBank();
	}
	
	public TransportDeck getTransportDeck() {
		return ((Bank)holdings.get(PlayerNumber.BANK)).getTransportDeck();
	}
	
	public TransportTradeOffer getTransportTradeOffer() {
		if (tradeOffer != null) {
			return tradeOffer.getTransporTradeOffer();
		}
		
		return null;
	}

	/**
	 * Initiates a transfer of cards between or among the bank and players'
	 * holdings
	 *
	 * @param invoice
	 *            an invoices to process
	 * @return indicates success of invoices
	 * @throws CatanException
	 *             if the invoice cannot be processed
	 * */
	public boolean processInvoice(ResourceInvoice invoice) throws CatanException {
		boolean success = false;
		Hand srcPlayer = this.holdings.get(invoice.getSourcePlayer());
		Hand dstPlayer = this.holdings.get(invoice.getDestinationPlayer());
		int brick = invoice.getBrick();
		int wood = invoice.getWood();
		int wheat = invoice.getWheat();
		int sheep = invoice.getSheep();
		int ore = invoice.getOre();
		if (brick != 0) {
			if (brick > 0) {
				success = this.tradeCards(srcPlayer, dstPlayer, ResourceType.BRICK, brick);
			}
			else {
				success = this.tradeCards(dstPlayer, srcPlayer, ResourceType.BRICK, -brick);
			}
		}
		if (wood != 0) {
			if (wood > 0) {
				success = this.tradeCards(srcPlayer, dstPlayer, ResourceType.WOOD, wood);
			}
			else {
				success = this.tradeCards(dstPlayer, srcPlayer, ResourceType.WOOD, -wood);
			}
		}
		if (wheat != 0) {
			if (wheat > 0) {
				success = this.tradeCards(srcPlayer, dstPlayer, ResourceType.WHEAT, wheat);
			}
			else {
				success = this.tradeCards(dstPlayer, srcPlayer, ResourceType.WHEAT, -wheat);
			}
		}
		if (sheep != 0) {
			if (sheep > 0) {
				success = this.tradeCards(srcPlayer, dstPlayer, ResourceType.SHEEP, sheep);
			}
			else {
				success = this.tradeCards(dstPlayer, srcPlayer, ResourceType.SHEEP, -sheep);
			}
		}
		if (ore != 0) {
			if (ore > 0) {
				success = this.tradeCards(srcPlayer, dstPlayer, ResourceType.ORE, ore);
			}
			else {
				success = this.tradeCards(dstPlayer, srcPlayer, ResourceType.ORE, -ore);
			}
		}
		return success;
	}
	
	public ResourceInvoice randomRobPlayer(PlayerNumber playerIndex, PlayerNumber victim) {
		ResourceInvoice invoice = new ResourceInvoice(victim, playerIndex);
		
		if (holdings.get(victim).getResourceCardCount(ResourceType.BRICK) > 0) {
			invoice.setBrick(1);
		}
		else if (holdings.get(victim).getResourceCardCount(ResourceType.ORE) > 0) {
			invoice.setOre(1);
		}
		else if (holdings.get(victim).getResourceCardCount(ResourceType.SHEEP) > 0) {
			invoice.setSheep(1);
		}
		else if (holdings.get(victim).getResourceCardCount(ResourceType.WHEAT) > 0) {
			invoice.setWheat(1);
		}
		else if (holdings.get(victim).getResourceCardCount(ResourceType.WOOD) > 0) {
			invoice.setWood(1);
		}
		else {
			return null;
		}
		
		return invoice;
	}
	
	public void processMonopoly(PlayerNumber player, ResourceType resource) throws CatanException {
		for (Map.Entry<PlayerNumber, Hand> holding : holdings.entrySet()) {
			if (holding.getKey() != player 
					&& holding.getKey() != PlayerNumber.BANK) {
				ResourceInvoice invoice = new ResourceInvoice(holding.getKey(), player);
				
				switch (resource) {
				case BRICK:
					invoice.setBrick(holding.getValue().getResourceCardCount(resource));
					break;
				case ORE:
					invoice.setOre(holding.getValue().getResourceCardCount(resource));
					break;
				case SHEEP:
					invoice.setSheep(holding.getValue().getResourceCardCount(resource));
					break;
				case WHEAT:
					invoice.setWheat(holding.getValue().getResourceCardCount(resource));
					break;
				case WOOD:
					invoice.setWood(holding.getValue().getResourceCardCount(resource));
					break;
				default:
					break;
				}
				
				this.processInvoice(invoice);
			}
		}
		
		((PlayerHoldings)holdings.get(player)).removeDevelopmentCard(DevCardType.MONOPOLY, 1);
	}

	/**
	 * @deprecated
	 * We dont need this because now process invoice does this all for us
	 *
	 * @param srcPlayer
	 * @param dstPlayer
	 * @param type
	 * @param count
	 * @return
	 * @throws CatanException
	 */
	private boolean tradeCards(
			Hand srcPlayer,
			Hand dstPlayer,
			ResourceType type,
			int count)
			throws CatanException {
		boolean success = false;

		//add to the logic if bank do special things
		Collection<ResourceCard> lostResources = srcPlayer.removeResourceCard(type, count);
		if (lostResources.isEmpty()) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"There were not enough cards in the hand to remove!");
		}
		success = dstPlayer.addResourceCardCollection(type, lostResources);
		if (!success) {
			srcPlayer.addResourceCardCollection(type, lostResources);
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"The cards were not added to the destination player!");
		}
		return success;
	}

	/**
	 * Returns whether the player can purchase the desired property
	 *
	 * @param player
	 *            the player
	 * @param type
	 *            the desired property type
	 * @return true if the player can purchase the desired property
	 */
	public boolean canPurchase(PlayerNumber player, PropertyType type) throws CatanException {
		boolean purchasable = false;
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"The bank cannot purchase resource cards");
		}
		Hand local = this.holdings.get(player);
		switch (type) {
		case ROAD:
			if (local.getResourceCardCount(ResourceType.BRICK) >= 1
					&& local.getResourceCardCount(ResourceType.WOOD) >= 1) {
				purchasable = true;
			}
			break;
		case CITY:
			if (local.getResourceCardCount(ResourceType.ORE) >= 3
					&& local.getResourceCardCount(ResourceType.WHEAT) >= 2) {
				purchasable = true;
			}
			break;
		case SETTLEMENT:
			if (local.getResourceCardCount(ResourceType.BRICK) >= 1
					&& local.getResourceCardCount(ResourceType.WOOD) >= 1
					&& local.getResourceCardCount(ResourceType.WHEAT) >= 1
					&& local.getResourceCardCount(ResourceType.SHEEP) >= 1) {
				purchasable = true;
			}
			break;
		case DEVELOPMENT_CARD:
			if (local.getResourceCardCount(ResourceType.SHEEP) >= 1
					&& local.getResourceCardCount(ResourceType.ORE) >= 1
					&& local.getResourceCardCount(ResourceType.WHEAT) >= 1) {
				purchasable = true;
			}
			break;
		}
		return purchasable;
	}

	/**
	 * Transfers the correct cards from a player's holdings to the bank for the
	 * given property
	 *
	 * @param player
	 *            the number of the player making the purchase
	 * @param propertyType
	 *            the type of property the player is purchasing
	 * @return indicates success of purchase
	 * @throws CatanException
	 *             if the player cannot purchase the property
	 */
	public void purchase(PlayerNumber player, PropertyType type) throws CatanException {
		ResourceInvoice purchase;
		if (!(this.canPurchase(player, type))) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Not enough resource cards to purchase");
		}
		switch (type) {
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
			((PlayerHoldings) this.holdings.get(player)).addDevelopmentCard(this
					.drawDevelopmentCard());
			break;
		}
	}

	private DevelopmentCard drawDevelopmentCard() {
		return ((Bank) this.holdings.get(PlayerNumber.BANK)).drawDevelopmentCard();
	}

	/**
	 * Returns whether the player can play the desired development card
	 *
	 * @param player
	 *            the player
	 * @param type
	 *            the desired development card type
	 * @return true if the player can play the desired development card
	 */
	public boolean canPlayDevelopmentCard(
			PlayerNumber player,
			DevCardType type) throws CatanException {
		boolean success = false;
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"The bank cannot play development cards.");
		}
		PlayerHoldings local = (PlayerHoldings) this.holdings.get(player);
		//check if the card is in the playable collections
		//need to check the playable boolean
		if (local.getDevelopmentCardCount(type) >= 1) {
			for (DevelopmentCard card : local.getDevelopmentCards().get(type)) {
				if (card.isPlayable()) {
					success = true;
					return success;
				}
			}
		}
		return success;
	}
	
	public int getDevelopmentCardCount(PlayerNumber player, DevCardType type) throws CatanException {
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"The bank cannot play development cards.");
		}
		
		PlayerHoldings holdings = (PlayerHoldings)this.holdings.get(player);
		
		int cardCount = holdings.getDevelopmentCardCount(type);
		
		return cardCount;
	}

	/**
	 * Exchanges resource cards from a player's holdings for a random
	 * development card from the bank
	 *
	 * @param player
	 *            the number of the player playing the development card
	 * @param type
	 *            the type of the development card being played
	 * @return indicates success of play
	 * @throws CatanException
	 *             if the player cannot play the desired development card
	 */
	public boolean playDevelopmentCard(
			PlayerNumber player,
			DevCardType type) throws CatanException {
		boolean beenPlayed = false;
		if (!(this.canPlayDevelopmentCard(player, type))) {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE,
					"The type of card is not in the player holdings");
		}
		Collection<DevelopmentCard> transDevCard = null;
		PlayerHoldings local = (PlayerHoldings) this.holdings.get(player);
		switch (type) {
		case SOLDIER:
			//move card to the soldier played deck
			transDevCard = local.removeDevelopmentCard(type, 1);
			if (transDevCard.isEmpty()) {
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"There were no Soldier cards to remove!");
			}
			beenPlayed = local.addDevelopmentCardCollection(type, transDevCard);
			if (!beenPlayed) {
				local.addDevelopmentCardCollection(type, transDevCard);
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"The Soldiers were not stored in the bank!");
			}
			break;
		case MONUMENT:
			//move the card to the monumnet played deck
			transDevCard = local.removeDevelopmentCard(type, 1);
			if (transDevCard.isEmpty()) {
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"There were no Moument cards to remove!");
			}
			beenPlayed = local.addDevelopmentCardCollection(type, transDevCard);
			if (!beenPlayed) {
				local.addDevelopmentCardCollection(type, transDevCard);
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"The Monuments were not stored in the discard!");
			}
			break;
		default:
			//move the card to the bank played deck
			transDevCard = local.removeDevelopmentCard(type, 1);
			if (transDevCard.isEmpty()) {
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"The cards were not removed!");
			}
			beenPlayed = this.holdings.get(PlayerNumber.BANK).addDevelopmentCardCollection(type,
					transDevCard);
			if (!beenPlayed) {
				local.addDevelopmentCardCollection(type, transDevCard);
				throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
						"The cards were not stored in the bank!");
			}
			break;
		}

		return beenPlayed;

	}

	/**
	 * tells if a Given player can use a special maritime trade
	 *
	 * @param player
	 * @return return true if he can false otherwise
	 */
	public boolean canMaritimeTrade(PlayerNumber player, ResourceType type) {
		boolean enough = false;
		int bestRatio = this.findBestRatio(player, type);
		if (this.holdings.get(player).getResourceCardCount(type) >= bestRatio) {
			enough = true;
			return enough;
		}
		return enough;
	}

	/**
	 * finds the best Maritime trade ratio for the given player if the player is
	 * the bank it will return a 0
	 *
	 * @param player
	 * @param type
	 * @return a 0 if fails a 2,3, or 4 for other one that is the ratio for the
	 *         given type
	 */
	public int findBestRatio(PlayerNumber player, ResourceType type) {
		if (player == PlayerNumber.BANK) {
			return 0;
		}
		int min = 4;
		PlayerHoldings hand = (PlayerHoldings) this.holdings.get(player);
		for (Harbor harbor : hand.getHarbors()) {
			if (harbor.getResource() == type) {
				return harbor.getRatio();
			}
			else if (harbor.getResource() == null) {
				//TODO the hosrbors get resource for this is null but should be all
				min = 3;
			}
		}
		return min;
	}

	/**
	 * This is a method to check if the source player in the invoice has the
	 * necessary resources to trade.
	 *
	 * @param invoice
	 * @return true if can offer trade false otherwise
	 */
	public boolean canOfferTrade(ResourceInvoice invoice) {
		PlayerNumber sourcePlayer = invoice.getSourcePlayer();
		PlayerHoldings sourceHand = (PlayerHoldings) this.holdings.get(sourcePlayer);

		for (ResourceType type : ResourceType.values()) {
			if (type == ResourceType.ALL || type == ResourceType.NONE) {
				continue;
			}
			int numSourceOffering = invoice.getResource(type);
			int numSourceAvailable = sourceHand.getResourceCardCount(type);
			if (numSourceOffering > 0 && numSourceAvailable < numSourceOffering) {
				return false;
			}
		}

		return true;
	}

	/**
	 * This method will check the destination player to see if they have enough
	 * resources to complete the trade.
	 *
	 * @param invoice
	 * @return return true if the dest player has the needed resources false
	 *         otherwise.
	 */
	public boolean canAcceptTrade(ResourceInvoice invoice) {
		PlayerNumber destPlayer = invoice.getDestinationPlayer();
		PlayerHoldings destHand = (PlayerHoldings) this.holdings.get(destPlayer);

		for (ResourceType type : ResourceType.values()) {
			if (type == ResourceType.ALL || type == ResourceType.NONE) {
				continue;
			}
			int numDestOffering = -invoice.getResource(type);
			int numDestAvailable = destHand.getResourceCardCount(type);
			if (numDestOffering > 0 && numDestAvailable < numDestOffering) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Determines whether the player has adequate resources.
	 *
	 * @param resourceType
	 * @param requiredQuantity
	 * @return hasNecessaryResource
	 */
	public boolean hasNecessaryResourceAmount(
			PlayerNumber player,
			ResourceType resourceType,
			int requiredQuantity) {

		if (this.holdings.get(player).getResourceCardCount(resourceType) >= requiredQuantity) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player has at least one harbor
	 *
	 * @param player
	 * @return
	 * @throws CatanException
	 */
	public boolean hasHarbor(PlayerNumber player) throws CatanException {
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot call hasHarbor() on BANK");
		}

		PlayerHoldings playerHoldings = (PlayerHoldings) this.holdings.get(player);
		if (playerHoldings.getHarbors().size() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Determines if a player has at least one development card
	 *
	 * @param player
	 * @return
	 */
	public boolean hasDevelopmentCard(PlayerNumber player) {
		if (this.holdings.get(player).getDevelopmentCardCount(null) > 0) {
			return true;
		}

		return false;
	}

	public void makeDevelopmentCardsPlayable(PlayerNumber player) throws CatanException {
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot call makeDevelopmentCardsPlayable() on BANK");
		}

		PlayerHoldings playerHoldings = (PlayerHoldings) this.holdings.get(player);
		playerHoldings.makeDevelopementCardsPlayable();
	}

	public Map<PlayerNumber, Hand> getHoldings() {
		return this.holdings;
	}

	public int getResourceCardCount(PlayerNumber player, ResourceType resource) {
		return this.getHoldings().get(player).getResourceCardCount(resource);
	}

	public int getNumberToDiscard(PlayerNumber playerIndex) {
		int numberOfCards = this.holdings.get(playerIndex).getResourceCardCount(ResourceType.ALL); 
		return ((numberOfCards > 7) ? (numberOfCards / 2) : 0);
	}
	
	public int getNumberOfPlayedSoldiers(PlayerNumber player) throws CatanException {
		if (player == PlayerNumber.BANK) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Cannot call getNumberOfPlayedSoldiers on BANK");
		}
		PlayerHoldings playerHoldings = (PlayerHoldings) this.holdings.get(player);
		return playerHoldings.getPlayedKnights().size();
	}
}
