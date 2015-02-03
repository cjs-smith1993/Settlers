package clientBackend.model;

import java.util.Collection;
import java.util.Map;

import shared.definitions.*;

/**
 * Represents the bank, which manages the collections of available resource and
 * development cards, as well as played development cards
 */
public class Bank {
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
	 * Returns the number of available development cards in the bank
	 * @return the number of available development cards
	 */
	public int getDevelopmentCardCound() {
		return 0;
	}
	
	/**
	 * Returns a resource card of the desired type from the corresponding deck
	 * @param player
	 * @param type
	 */
	public void drawResourceCard(PlayerNumber player, ResourceType type) {
		
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
}