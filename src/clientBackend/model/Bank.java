package clientBackend.model;

import java.util.Collection;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getDevelopmentCardCount(DevCardType type) {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * Returns the number of available resource cards of the desired type in
	 * the bank
	 * @param type the desired resource type
	 * @return the number of available resource cards of the desired type
	 */
	public int getResourceCardCount(ResourceType type) {
		return 0;
	}

	@Override
	public boolean addResourceCardCollection(ResourceType type,
			Collection<ResourceCard> newCards) {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public boolean addDevelopmentCardCollection(DevCardType type,
			Collection<DevelopmentCard> newCards) {
		// TODO Auto-generated method stub
		return false;
	}
}
