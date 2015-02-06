package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import clientBackend.transport.TransportBank;
import clientBackend.transport.TransportDeck;
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
	 * Default constructor for the bank will create all the pieces needed
	 * Not Implemented
	 */
	public Bank()
	{
		//make all the cards put them in the space
		for(ResourceType type: ResourceType.values())
		{
			resourceCards.get(type).addAll(makeTypeDeck(type));
		}
		
		for(DevCardType type: DevCardType.values())
		{
			switch(type){
			case SOLDIER:
				developmentCards.get(type).addAll(makeDevelopmentDeck(type, 14));
				break;
			case MONUMENT:
				developmentCards.get(type).addAll(makeDevelopmentDeck(type, 5));
				break;
			default:
				developmentCards.get(type).addAll(makeDevelopmentDeck(type, 2));
				break;
			}
		}
		
	}
	
	public Bank(TransportDeck devDeck, TransportBank resDeck)
	{
		//resource cards
		makeResourceDeck(ResourceType.BRICK, resDeck.brick);
		makeResourceDeck(ResourceType.WOOD, resDeck.wood);
		makeResourceDeck(ResourceType.WHEAT, resDeck.wheat);
		makeResourceDeck(ResourceType.SHEEP, resDeck.sheep);
		makeResourceDeck(ResourceType.ORE, resDeck.ore);
		//Development cards
		makeDevTypePile(DevCardType.MONOPOLY, devDeck.monopoly);
		makeDevTypePile(DevCardType.MONUMENT, devDeck.monument);
		makeDevTypePile(DevCardType.ROAD_BUILD, devDeck.roadBuilding);
		makeDevTypePile(DevCardType.SOLDIER, devDeck.soldier);
		makeDevTypePile(DevCardType.YEAR_OF_PLENTY, devDeck.yearOfPlenty);
		
	}
	private void makeDevTypePile(DevCardType type, int count)
	{
		Collection<DevelopmentCard> newPile = new ArrayList<DevelopmentCard>();
		for(int i = 0; i < count; ++i)
		{
			newPile.add(new DevelopmentCard(type));
		}
		developmentCards.get(type).clear();
		developmentCards.get(type).addAll(newPile);
		newPile.clear();
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
	private Collection<DevelopmentCard> makeDevelopmentDeck(
			DevCardType type, int j) {
		Collection<DevelopmentCard> typeDeck = new ArrayList<DevelopmentCard>();
		for(int i = 0; i < j; i++)
		{
			typeDeck.add(new DevelopmentCard(type));
		}
		return typeDeck;
	}
	private Collection<ResourceCard> makeTypeDeck(ResourceType type) {
		Collection<ResourceCard> typeDeck = new ArrayList<ResourceCard>();
		for(int i = 0; i < 25; i++)
		{
			typeDeck.add(new ResourceCard(type));
		}
		return typeDeck;
	}
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
	 * Returns the number of available resource cards of the desired type in
	 * the bank
	 * @param type the desired resource type
	 * @return the number of available resource cards of the desired type
	 */
	public int getResourceCardCount(ResourceType type) {
		return resourceCards.get(type).size();
	}

	
}
