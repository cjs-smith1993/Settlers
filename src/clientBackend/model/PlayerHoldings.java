package clientBackend.model;

import java.util.Collection;
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
		return 0;
	}
	
	/**
	 * Returns the number of resource cards of the desired type in the player's
	 * hand
	 * @param type the desired type
	 * @return the number of resource cards
	 */
	public int getResourceCardCount(ResourceType type) {
		return 0;
	}
	/**
	 * This remove the number of resourec cards specified by count in a collection object
	 * @param type
	 * @param count
	 * @return this will return a collection of ResourceCard objects to be added to another PlayerHoldings
	 */
	public Collection<ResourceCard> removeResourceCard (ResourceType type, int count)
	{
		return null;
	}
	/**
	 * this will add a collection of cards to a players holding
	 * @param newCards
	 */
	
	public boolean addResourceCardCollection (Collection<ResourceCard> newCards)
	{
		boolean added = true;
		return added;
	}
}
