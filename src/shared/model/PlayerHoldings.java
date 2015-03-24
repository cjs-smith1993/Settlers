package shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import shared.definitions.*;
import shared.transport.TransportNewDevCards;
import shared.transport.TransportOldDevCards;
import shared.transport.TransportPlayer;
import shared.transport.TransportResources;

/**
 * An encapsulation of the cards that a player holds, the knights and monuments
 * that he has played, and the harbors that he owns
 */
public class PlayerHoldings implements Hand {
	private Map<ResourceType, Collection<ResourceCard>> resourceCards;
	private Map<DevCardType, Collection<DevelopmentCard>> developmentCards;
	private Collection<DevelopmentCard> playedKnights;
	private Collection<DevelopmentCard> playedMonuments;
	private Collection<Harbor> harbors;
	
	/**
	 * Default constructor for the PlayerHoldings class.
	 */
	public PlayerHoldings() {
		resourceCards = new HashMap<ResourceType,Collection<ResourceCard>>();
		for(ResourceType type: ResourceType.values()){
			resourceCards.put(type, new ArrayList<ResourceCard>());
		}
		developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		for(DevCardType type: DevCardType.values()){
			developmentCards.put(type, new ArrayList<DevelopmentCard>());
		}
		playedKnights = new ArrayList<DevelopmentCard>();
		playedMonuments = new ArrayList<DevelopmentCard>();
		harbors = new ArrayList<Harbor>();
		
	}
	
	public PlayerHoldings(TransportPlayer player,
							Collection<Harbor> myHarbors) {
		
		resourceCards = new HashMap<ResourceType,Collection<ResourceCard>>();
		for(ResourceType type: ResourceType.values()){
			resourceCards.put(type, new ArrayList<ResourceCard>());
		}
		developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		for(DevCardType type: DevCardType.values()){
			developmentCards.put(type, new ArrayList<DevelopmentCard>());
		}
		playedKnights = new ArrayList<DevelopmentCard>();
		playedMonuments = new ArrayList<DevelopmentCard>();
		harbors = new ArrayList<Harbor>();
		int playedSoldier = player.soldiers;
		int monuments = player.monuments;
		TransportResources myResources = player.resources; 
		TransportOldDevCards playableDev = player.oldDevCards;
		TransportNewDevCards blockedDev = player.newDevCards;
		makeResourceDeck(ResourceType.BRICK, myResources.brick);
		makeResourceDeck(ResourceType.WOOD, myResources.wood);
		makeResourceDeck(ResourceType.WHEAT, myResources.wheat);
		makeResourceDeck(ResourceType.SHEEP, myResources.sheep);
		makeResourceDeck(ResourceType.ORE, myResources.ore);
		
		if(myHarbors != null)
			this.setHarbors(myHarbors);
		else
			harbors = new ArrayList<Harbor>();
		this.setPlayedKnights(makeDevTypePile(DevCardType.SOLDIER, playedSoldier, false));
		this.setPlayedMonuments(makeDevTypePile(DevCardType.MONUMENT,monuments, false));
		
		//Development cards
		developmentCards.get(DevCardType.MONOPOLY).clear();
		developmentCards.get(DevCardType.MONOPOLY).addAll(makeDevTypePile(DevCardType.MONOPOLY, playableDev.monopoly, true));
		developmentCards.get(DevCardType.MONOPOLY).addAll(makeDevTypePile(DevCardType.MONOPOLY, blockedDev.monopoly, false));
		developmentCards.get(DevCardType.MONUMENT).clear();
		developmentCards.get(DevCardType.MONUMENT).addAll(makeDevTypePile(DevCardType.MONUMENT, playableDev.monument, true));
		developmentCards.get(DevCardType.MONUMENT).addAll(makeDevTypePile(DevCardType.MONUMENT, blockedDev.monument, false));
		developmentCards.get(DevCardType.ROAD_BUILD).clear();	
		developmentCards.get(DevCardType.ROAD_BUILD).addAll(makeDevTypePile(DevCardType.ROAD_BUILD, playableDev.roadBuilding, true));	
		developmentCards.get(DevCardType.ROAD_BUILD).addAll(makeDevTypePile(DevCardType.ROAD_BUILD, blockedDev.roadBuilding, false));
		developmentCards.get(DevCardType.SOLDIER).clear();	
		developmentCards.get(DevCardType.SOLDIER).addAll(makeDevTypePile(DevCardType.SOLDIER, playableDev.soldier, true));
		developmentCards.get(DevCardType.SOLDIER).addAll(makeDevTypePile(DevCardType.SOLDIER, blockedDev.soldier, false));
		developmentCards.get(DevCardType.YEAR_OF_PLENTY).clear();
		developmentCards.get(DevCardType.YEAR_OF_PLENTY).addAll(makeDevTypePile(DevCardType.YEAR_OF_PLENTY, playableDev.yearOfPlenty,true));
		developmentCards.get(DevCardType.YEAR_OF_PLENTY).addAll(makeDevTypePile(DevCardType.YEAR_OF_PLENTY, blockedDev.yearOfPlenty,false));
	}
	
	/**
	 * Extracts the TransportPlayer .soldiers and .monuments information from PlayerHoldings into the TransportPlayer.
	 * @param player
	 * @return TransportPlayer
	 */
	public TransportPlayer getTransportPlayer(TransportPlayer player) {
		player.soldiers = playedKnights.size();
		player.monuments = playedMonuments.size();
		
		return player;
	}
	
	/**
	 * Extract information to the TransportResources container for serialization.
	 * @return TransportResources
	 */
	public TransportResources getTransportResources() {
		TransportResources resources = new TransportResources();
		
		resources.brick = resourceCards.get(ResourceType.BRICK).size();
		resources.ore = resourceCards.get(ResourceType.ORE).size();
		resources.sheep = resourceCards.get(ResourceType.SHEEP).size();
		resources.wheat = resourceCards.get(ResourceType.WHEAT).size();
		resources.wood = resourceCards.get(ResourceType.WOOD).size();
		
		return resources;
	}
	
	/**
	 * Extract information to the TransportOldDevCards container for serialization.
	 * @return TransportOldDevCards
	 */
	public TransportOldDevCards getTransportOldDevCards() {
		TransportOldDevCards oldDevCards = new TransportOldDevCards();
		
		oldDevCards.monopoly = getDevCardCount(developmentCards.get(DevCardType.MONOPOLY), true);
		oldDevCards.monument = getDevCardCount(developmentCards.get(DevCardType.MONUMENT), true);
		oldDevCards.roadBuilding = getDevCardCount(developmentCards.get(DevCardType.ROAD_BUILD), true);
		oldDevCards.soldier = getDevCardCount(developmentCards.get(DevCardType.SOLDIER), true);
		oldDevCards.yearOfPlenty = getDevCardCount(developmentCards.get(DevCardType.YEAR_OF_PLENTY), true);
		
		return oldDevCards;
	}
	
	/**
	 * Extract information to the TransportNewDevCards container for serialization.
	 * @return TransportNewDevCards
	 */
	public TransportNewDevCards getTransportNewDevCards() {
		TransportNewDevCards newDevCards = new TransportNewDevCards();
		
		newDevCards.monopoly = getDevCardCount(developmentCards.get(DevCardType.MONOPOLY), false);
		newDevCards.monument = getDevCardCount(developmentCards.get(DevCardType.MONUMENT), false);
		newDevCards.roadBuilding = getDevCardCount(developmentCards.get(DevCardType.ROAD_BUILD), false);
		newDevCards.soldier = getDevCardCount(developmentCards.get(DevCardType.SOLDIER), false);
		newDevCards.yearOfPlenty = getDevCardCount(developmentCards.get(DevCardType.YEAR_OF_PLENTY), false);
		
		return newDevCards;
	}
	
	/**
	 * Count number of playable (old) development cards in the deck.
	 * @param developmentCards
	 * @return playableDevelopmentCardCount
	 */
	public int getDevCardCount(Collection<DevelopmentCard> developmentCards, boolean isPlayable) {
		int cardCount = 0;
		
		for (DevelopmentCard developmentCard : developmentCards) {
			if (developmentCard.isPlayable() == isPlayable) {
				cardCount++;
			}
		}
		
		return cardCount;
	}
	
	private Collection<DevelopmentCard> makeDevTypePile(DevCardType type, int count, boolean playable) {
		Collection<DevelopmentCard> newPile = new ArrayList<DevelopmentCard>();
		for(int i = 0; i < count; ++i)
		{
			newPile.add(new DevelopmentCard(type,playable));
		}
		return newPile;
	}
	
	private void makeResourceDeck(ResourceType type, int count) {
		Collection<ResourceCard> newPile = new ArrayList<ResourceCard>();
		for(int i = 0; i < count; ++i)
		{
			newPile.add(new ResourceCard(type));
		}
		resourceCards.get(type).clear();
		resourceCards.get(type).addAll(newPile);
		newPile.clear();
	}
	
	// How is connor telling that the house is on a harbor
	/**
	 * Returns the number of development cards of the desired type in the
	 * player's hand
	 * @param type the desired type
	 * @return the number of development cards
	 */
	public int getDevelopmentCardCount(DevCardType type) {
		
		if (type == null) {
			int total = 0;
			for (Collection<DevelopmentCard> cards : developmentCards.values()) {
				total += cards.size();
			}
			return total;
		}
		
		return developmentCards.get(type).size();
	}
	
	/**
	 * Returns the number of resource cards of the desired type in the player's
	 * hand
	 * @param type the desired type
	 * @return the number of resource cards
	 */
	public int getResourceCardCount(ResourceType type) {
		if (type == ResourceType.ALL) {
			int totalCards = 0;
			for (ResourceType resourceType : ResourceType.values()) {
				if (resourceType != ResourceType.ALL && resourceType != ResourceType.NONE) {
					totalCards += resourceCards.get(resourceType).size();
				}
			}
			return totalCards;
		}
		else {
			return resourceCards.get(type).size();
		}
	}
	
	/**
	 * This remove the number of resource cards specified by count in a collection object
	 * @param type
	 * @param count
	 * @return this will return a collection of ResourceCard objects to be added to another PlayerHoldings
	 */
	public Collection<ResourceCard> removeResourceCard (ResourceType type, int count) {
		Collection<ResourceCard> removed = new ArrayList<ResourceCard>();
        
        Collection<ResourceCard> cards = this.resourceCards.get(type);
        if(!cards.isEmpty()){
        	for (int i = 0; i < count; i++) {
        		Iterator<ResourceCard> it = cards.iterator();
        		ResourceCard card = it.next();
        		it.remove();
        		removed.add(card);
        	}
        }
        return removed;
	}
	
	/**
	 * this will add a collection of cards to a players holding
	 * @param newCards
	 */
	public boolean addResourceCardCollection (ResourceType type, Collection<ResourceCard> newCards)
	{
		boolean added = true;
		if(!resourceCards.get(type).addAll(newCards))
		{
			added = false;
		}
		return added;
	}
	
	// Do that same for dev cards as for resource cards.
	/**
	 * This remove the number of development cards specified by count in a collection object
	 * @param type the type of dev card 
	 * @param count the number of that card to move
	 * @return this will return a collection of DevelopmentCard objects to be added to another place
	 */
	public Collection<DevelopmentCard> removeDevelopmentCard (DevCardType type, int count)
	{
		Collection<DevelopmentCard> removed = new ArrayList<DevelopmentCard>();
        
        Collection<DevelopmentCard> cards = this.developmentCards.get(type);
        if(!cards.isEmpty())
        	for (int i = 0; i < count; i++) {
        		Iterator<DevelopmentCard> it = cards.iterator();
        		DevelopmentCard card = it.next();
        		it.remove();
        		removed.add(card);
        	}
        return removed;
	}
	
	/**
	 * this will add a collection of cards to a players holding
	 * @param type the type of the development card added
	 * @param newCards a collection of cards to add
	 */
	public boolean addDevelopmentCardCollection (DevCardType type, Collection<DevelopmentCard> newCards) {
		//i need to deal with the fact that i can and need to store monuments and soldier but not the others
		
		boolean added = true;
		switch(type){
		case SOLDIER:
			playedKnights.addAll(newCards);
			break;
		case MONUMENT:
			playedMonuments.addAll(newCards);
			break;
		default:
			added = false;
			
		}
		
		return added;
	}
	
	public void makeDevelopementCardsPlayable() {
		for (Collection<DevelopmentCard> cards : developmentCards.values()) {
			for (DevelopmentCard card : cards) {
				card.setPlayable(true);
			}
		}
	}
	
	// Need the harbor stuff
	public Collection<DevelopmentCard> getPlayedKnights() {
		return playedKnights;
	}

	public void setPlayedKnights(Collection<DevelopmentCard> playedKnights) {
		this.playedKnights = playedKnights;
	}

	public Collection<DevelopmentCard> getPlayedMonuments() {
		return playedMonuments;
	}

	public void setPlayedMonuments(Collection<DevelopmentCard> playedMonuments) {
		this.playedMonuments = playedMonuments;
	}

	public Collection<Harbor> getHarbors() {
		return harbors;
	}

	public void setHarbors(Collection<Harbor> harbors) {
		this.harbors = harbors;
	}

	public Map<ResourceType, Collection<ResourceCard>> getResourceCards() {
		return resourceCards;
	}
	
	public void setResourceCards(
			Map<ResourceType, Collection<ResourceCard>> resourceCards) {
		this.resourceCards = resourceCards;
	}
	
	public Map<DevCardType, Collection<DevelopmentCard>> getDevelopmentCards() {
		return developmentCards;
	}

	public void setDevelopmentCards(
			Map<DevCardType, Collection<DevelopmentCard>> developmentCards) {
		this.developmentCards = developmentCards;
	}
	
	public void addDevelopmentCard(DevelopmentCard devCard) {
		DevCardType type = devCard.getType();
		developmentCards.get(type).add(devCard);
	}
		
}
