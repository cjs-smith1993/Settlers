package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
		resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		playedCards = new ArrayList<DevelopmentCard>();
		//make all the cards put them in the space
		for(ResourceType type: ResourceType.values())
		{
			resourceCards.put(type, makeTypeDeck(type, 25));
		}
		
		for(DevCardType type: DevCardType.values())
		{
			switch(type){
			case SOLDIER:
				developmentCards.put(type, makeDevelopmentDeck(type, 14));
				break;
			case MONUMENT:
				developmentCards.put(type, makeDevelopmentDeck(type, 5));
				break;
			default:
				developmentCards.put(type, makeDevelopmentDeck(type, 2));
				break;
			}
		}
		
	}
	
	public Bank(TransportDeck devDeck, TransportBank resDeck)
	{
		resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		playedCards = new ArrayList<DevelopmentCard>();
		//resource cards
		for(ResourceType type: ResourceType.values()){
			switch(type){
			case BRICK:
				resourceCards.put(type, makeTypeDeck(type, resDeck.brick));
				break;
			case WOOD:
				resourceCards.put(type, makeTypeDeck(type, resDeck.wood));
				break;
			case WHEAT:
				resourceCards.put(type, makeTypeDeck(type, resDeck.wheat));
				break;
			case ORE:
				resourceCards.put(type, makeTypeDeck(type, resDeck.ore));
				break;
			case SHEEP:
				resourceCards.put(type, makeTypeDeck(type, resDeck.sheep));
				break;
			default:
				resourceCards.put(type, makeTypeDeck(type, 0));
				break;
			}
		}
		for(DevCardType type: DevCardType.values())
		{
			switch(type){
			case SOLDIER:
				developmentCards.put(type, makeDevelopmentDeck(type, devDeck.soldier));
				break;
			case MONUMENT:
				developmentCards.put(type, makeDevelopmentDeck(type, devDeck.monument));
				break;
			case YEAR_OF_PLENTY:
				developmentCards.put(type, makeDevelopmentDeck(type, devDeck.yearOfPlenty));
				break;
			case MONOPOLY:
				developmentCards.put(type, makeDevelopmentDeck(type, devDeck.monopoly));
				break;
			case ROAD_BUILD:
				developmentCards.put(type, makeDevelopmentDeck(type, devDeck.roadBuilding));
				break;
			default:
				developmentCards.put(type, makeDevelopmentDeck(type, 2));
				break;
			}
		}
		//Development cards
		
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
	private Collection<ResourceCard> makeTypeDeck(ResourceType type, int count) {
		Collection<ResourceCard> typeDeck = new ArrayList<ResourceCard>();
		if(count < 0) {
			count = 25;
		}
		for(int i = 0; i < count; i++) {
			typeDeck.add(new ResourceCard(type));
		}
		return typeDeck;
	}
	/**
	 * Returns a development card from the deck
	 * @param player
	 */
	public DevelopmentCard drawDevelopmentCard() {
		RandomNumberGenerator randNum = new RandomNumberGenerator();
		int rand = randNum.generate(0, getDevelopmentCardCount(null));
		int soldierCount = getDevelopmentCardCount(DevCardType.SOLDIER);
		int monoplyCount = getDevelopmentCardCount(DevCardType.MONOPOLY);
		int monumentCount = getDevelopmentCardCount(DevCardType.MONUMENT);
		int roadBuildCount = getDevelopmentCardCount(DevCardType.ROAD_BUILD);
		int yearofPlentyCount = getDevelopmentCardCount(DevCardType.YEAR_OF_PLENTY);
		if(rand < soldierCount){
			return getDevelopmentCard(DevCardType.SOLDIER);
		}
		rand -= (soldierCount-1);
		if(rand < monoplyCount){
			return getDevelopmentCard(DevCardType.MONOPOLY);
		}
		rand -= (monoplyCount-1);
		if(rand < roadBuildCount){
			return getDevelopmentCard(DevCardType.ROAD_BUILD);
		}
		rand -= (roadBuildCount-1);
		if(rand < yearofPlentyCount){
			return getDevelopmentCard(DevCardType.YEAR_OF_PLENTY);
		}
		rand -= (yearofPlentyCount-1);
		if(rand < monumentCount){
			return getDevelopmentCard(DevCardType.MONOPOLY);
		}
		return null;
	}
	private DevelopmentCard getDevelopmentCard(DevCardType type){
		Iterator<DevelopmentCard> iter = developmentCards.get(type).iterator();
		DevelopmentCard card = iter.next();
		iter.remove();
		return card;
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
		boolean added = false;
		if(type != DevCardType.SOLDIER || type != DevCardType.MONUMENT)
		{
			added = playedCards.addAll(newCards);
			
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

	public Collection<DevelopmentCard> getPlayedCards() {
		return playedCards;
	}

	public void setPlayedCards(Collection<DevelopmentCard> playedCards) {
		this.playedCards = playedCards;
	}

	
}
