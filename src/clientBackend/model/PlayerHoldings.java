package clientBackend.model;

import java.util.Collection;
import java.util.Map;

import shared.definitions.*;

/**
 * An encapsulation of the cards that a player holds, the knights and monuments
 * that he has played, and the harbors that he owns TODO
 */
public class PlayerHoldings {
	private Map<ResourceType, Collection<ResourceCard>> resourceCards;
	private Map<DevCardType, Collection<DevelopmentCard>> developmentCards;
	private Collection<DevelopmentCard> playedKnights;
	private Collection<DevelopmentCard> playedMonuments;
	private Collection<Harbor> harbors;
}
