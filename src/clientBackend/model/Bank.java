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
	 * TODO
	 * @param player
	 */
	public void drawDevelopmentCard(PlayerNumber player) {
		
	}
	
	public void drawResourceCard(PlayerNumber player, ResourceType type) {
		
	}
}
