package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import shared.definitions.*;

/**
 * An encapsulation of the cards that a player holds, the knights and monuments
 * that he has played, and the harbors that he owns
 */
public class PlayerHoldings {
	private Map<ResourceType, Collection<ResourceCard>> resourceCards;
	private Map<DevCardType, Collection<DevelopmentCard>> developmentCards;
	private Collection<DevelopmentCard> playedKnights;
	private Collection<DevelopmentCard> playedMonuments;
	private Collection<Harbor> harbors;
	
	/**
	 * Returns the number of development cards of the desired type in the
	 * player's hand
	 * @param type the desired type
	 * @return the number of development cards
	 */
	public int getDevelopmentCardCount(DevCardType type) {
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
	 * This remove the number of resourec cards specified by count in a collection object
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
	 * @param newCards
	 */
	
	public boolean addDevelopmentCardCollection (DevCardType type, Collection<DevelopmentCard> newCards)
	{
		boolean added = true;
		if(!developmentCards.get(type).addAll(newCards))
		{
			added = false;
		}
		return added;
	}

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
	
	
}
