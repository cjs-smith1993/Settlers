package clientBackend.model;

import java.util.Collection;
import java.util.Map;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;


public interface Hand {
	Map<ResourceType, Collection<ResourceCard>> resourceCards = null;
	Map<DevCardType, Collection<DevelopmentCard>> developmentCards = null;
	Collection<DevelopmentCard> playedCards = null;
	
	public int getDevelopmentCardCount(DevCardType type);
	public int getResourceCardCount(ResourceType type);
	public Collection<ResourceCard> removeResourceCard (ResourceType type, int count);
	public boolean addResourceCardCollection (ResourceType type, Collection<ResourceCard> newCards);
	public boolean addDevelopmentCardCollection (DevCardType type, Collection<DevelopmentCard> newCards);
}
