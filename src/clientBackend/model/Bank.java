package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import shared.definitions.*;

/**
 * Represents the bank, which manages the collections of available resource and
 * development cards, as well as played development cards
 */
public class Bank implements Hand{
	private Map<ResourceType, Collection<ResourceCard>> resourceCards;
	private Map<DevCardType, Collection<DevelopmentCard>> developmentCards;
	private Collection<DevelopmentCard> playedCards;
	
	/**
	 * Returns a development card from the deck
	 * @param player
	 */
	public DevelopmentCard drawDevelopmentCard() {
		return null;
	}
	
	/**
	 * Returns a resource card of the desired type from the corresponding deck
	 * @param player
	 * @param type
	 */
	@Override
	public Collection<ResourceCard> removeResourceCard(ResourceType type,
			int count) {
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
	
	@Override
	public boolean addResourceCardCollection(ResourceType type,
			Collection<ResourceCard> newCards) {
		boolean added = true;
		if(!resourceCards.get(type).addAll(newCards))
		{
			added = false;
		}
		return added;
	}

	@Override
	public boolean addDevelopmentCardCollection(DevCardType type,
			Collection<DevelopmentCard> newCards) {
		boolean added = true;
		if(!playedCards.addAll(newCards))
		{
			added = false;
		}
		return added;
	}
	
	@Override
	public int getDevelopmentCardCount(DevCardType type) {
		return developmentCards.get(type).size();
	}
	/**
	 * Returns the number of available resource cards of the desired type in
	 * the bank
	 * @param type the desired resource type
	 * @return the number of available resource cards of the desired type
	 */
	public int getResourceCardCount(ResourceType type) {
		return resourceCards.get(type).size();
	}

	
}
