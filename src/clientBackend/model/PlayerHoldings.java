package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import clientBackend.transport.TransportNewDevCards;
import clientBackend.transport.TransportOldDevCards;
import clientBackend.transport.TransportPlayer;
import clientBackend.transport.TransportResources;
import shared.definitions.*;

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
	 * Default constructor for the PlayerHolings class.
	 * Not Implemented
	 */
	public PlayerHoldings()
	{
		resourceCards = new HashMap<ResourceType,Collection<ResourceCard>>();
		developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		playedKnights = new ArrayList<DevelopmentCard>();
		playedMonuments = new ArrayList<DevelopmentCard>();
		harbors = new ArrayList<Harbor>();
		
	}
	public PlayerHoldings(TransportPlayer player,
							Collection<Harbor> myHarbors)
	{
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
		
		this.setHarbors(myHarbors);
		this.setPlayedKnights(makeDevTypePile(DevCardType.SOLDIER, playedSoldier, false));
		this.setPlayedMonuments(makeDevTypePile(DevCardType.MONUMENT,monuments, false));
		
		//Development cards
		developmentCards.get(playableDev.monopoly).clear();
		developmentCards.get(playableDev.monopoly).addAll(makeDevTypePile(DevCardType.MONOPOLY, playableDev.monopoly, true));
		developmentCards.get(playableDev.monopoly).addAll(makeDevTypePile(DevCardType.MONOPOLY, blockedDev.monopoly, false));
		developmentCards.get(playableDev.monument).clear();
		developmentCards.get(playableDev.monument).addAll(makeDevTypePile(DevCardType.MONUMENT, playableDev.monument, true));
		developmentCards.get(playableDev.monument).addAll(makeDevTypePile(DevCardType.MONUMENT, playableDev.monument, false));
		developmentCards.get(playableDev.roadBuilding).clear();	
		developmentCards.get(playableDev.roadBuilding).addAll(makeDevTypePile(DevCardType.ROAD_BUILD, playableDev.roadBuilding, true));	
		developmentCards.get(playableDev.roadBuilding).addAll(makeDevTypePile(DevCardType.ROAD_BUILD, playableDev.roadBuilding, false));
		developmentCards.get(playableDev.soldier).clear();	
		developmentCards.get(playableDev.soldier).addAll(makeDevTypePile(DevCardType.SOLDIER, playableDev.soldier, true));
		developmentCards.get(playableDev.soldier).addAll(makeDevTypePile(DevCardType.SOLDIER, playableDev.soldier, false));
		developmentCards.get(playableDev.yearOfPlenty).clear();
		developmentCards.get(playableDev.yearOfPlenty).addAll(makeDevTypePile(DevCardType.YEAR_OF_PLENTY, playableDev.yearOfPlenty,true));
		developmentCards.get(playableDev.yearOfPlenty).addAll(makeDevTypePile(DevCardType.YEAR_OF_PLENTY, playableDev.yearOfPlenty,false));
		
	}
	private Collection<DevelopmentCard> makeDevTypePile(DevCardType type, int count, boolean playable)
	{
		Collection<DevelopmentCard> newPile = new ArrayList<DevelopmentCard>();
		for(int i = 0; i < count; ++i)
		{
			newPile.add(new DevelopmentCard(type,playable));
		}
		return newPile;
	}
	private void makeResourceDeck(ResourceType type, int count)
	{
		Collection<ResourceCard> newPile = new ArrayList<ResourceCard>();
		for(int i = 0; i < count; ++i)
		{
			newPile.add(new ResourceCard(type));
		}
		resourceCards.get(type).clear();
		resourceCards.get(type).addAll(newPile);
		newPile.clear();
	}
	//how is connor telling that the house is on a harbor
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
		return resourceCards.get(type).size();
	}
	
	/**
	 * This remove the number of resource cards specified by count in a collection object
	 * @param type
	 * @param count
	 * @return this will return a collection of ResourceCard objects to be added to another PlayerHoldings
	 */
	public Collection<ResourceCard> removeResourceCard (ResourceType type, int count)
	{
		Collection<ResourceCard> removed = new ArrayList<ResourceCard>();
        
        Collection<ResourceCard> cards = this.resourceCards.get(type);
        for (int i = 0; i < count; i++) {
            Iterator<ResourceCard> it = cards.iterator();
            ResourceCard card = it.next();
            it.remove();
            removed.add(card);
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
	
	//do that same for dev cards as for resource cards.
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
	
	public boolean addDevelopmentCardCollection (DevCardType type, Collection<DevelopmentCard> newCards)
	{
		//i need to deal witht he fact that i can and need to store monuments and soldier but not the others
		boolean added = true;
		if(!developmentCards.get(type).addAll(newCards))
		{
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
	
	//need the harbor stuff
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
		
}
