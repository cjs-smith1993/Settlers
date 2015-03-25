package shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import shared.definitions.*;
import shared.transport.TransportBank;
import shared.transport.TransportDeck;

/**
 * Represents the bank, which manages the collections of available resource and
 * development cards, as well as played development cards
 */
public class Bank implements Hand {
	private Map<ResourceType, Collection<ResourceCard>> resourceCards;
	private Map<DevCardType, Collection<DevelopmentCard>> developmentCards;
	private Collection<DevelopmentCard> playedCards;

	/**
	 * Default constructor for the bank will create all the pieces needed Not
	 * Implemented
	 */
	public Bank() {
		this.resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		this.developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		this.playedCards = new ArrayList<DevelopmentCard>();
		//make all the cards put them in the space
		for (ResourceType type : ResourceType.values())
		{
			this.resourceCards.put(type, this.makeTypeDeck(type, 25));
		}

		for (DevCardType type : DevCardType.values())
		{
			switch (type) {
			case SOLDIER:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, 14));
				break;
			case MONUMENT:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, 5));
				break;
			default:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, 2));
				break;
			}
		}

	}

	public Bank(TransportDeck devDeck, TransportBank resDeck) {
		this.resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		this.developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		this.playedCards = new ArrayList<DevelopmentCard>();
		//resource cards
		for (ResourceType type : ResourceType.values()) {
			switch (type) {
			case BRICK:
				this.resourceCards.put(type, this.makeTypeDeck(type, resDeck.brick));
				break;
			case WOOD:
				this.resourceCards.put(type, this.makeTypeDeck(type, resDeck.wood));
				break;
			case WHEAT:
				this.resourceCards.put(type, this.makeTypeDeck(type, resDeck.wheat));
				break;
			case ORE:
				this.resourceCards.put(type, this.makeTypeDeck(type, resDeck.ore));
				break;
			case SHEEP:
				this.resourceCards.put(type, this.makeTypeDeck(type, resDeck.sheep));
				break;
			default:
				this.resourceCards.put(type, this.makeTypeDeck(type, 0));
				break;
			}
		}

		for (DevCardType type : DevCardType.values()) {
			switch (type) {
			case SOLDIER:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, devDeck.soldier));
				break;
			case MONUMENT:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, devDeck.monument));
				break;
			case YEAR_OF_PLENTY:
				this.developmentCards.put(type,
						this.makeDevelopmentDeck(type, devDeck.yearOfPlenty));
				break;
			case MONOPOLY:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, devDeck.monopoly));
				break;
			case ROAD_BUILD:
				this.developmentCards.put(type,
						this.makeDevelopmentDeck(type, devDeck.roadBuilding));
				break;
			default:
				this.developmentCards.put(type, this.makeDevelopmentDeck(type, 2));
				break;
			}
		}
		//Development cards
	}

	public TransportBank getTransportBank() {
		TransportBank bank = new TransportBank();

		bank.brick = this.resourceCards.get(ResourceType.BRICK).size();
		bank.ore = this.resourceCards.get(ResourceType.ORE).size();
		bank.sheep = this.resourceCards.get(ResourceType.SHEEP).size();
		bank.wheat = this.resourceCards.get(ResourceType.WHEAT).size();
		bank.wood = this.resourceCards.get(ResourceType.WOOD).size();

		return bank;
	}

	public TransportDeck getTransportDeck() {
		TransportDeck deck = new TransportDeck();

		deck.monopoly = this.developmentCards.get(DevCardType.MONOPOLY).size();
		deck.monument = this.developmentCards.get(DevCardType.MONUMENT).size();
		deck.roadBuilding = this.developmentCards.get(DevCardType.ROAD_BUILD).size();
		deck.soldier = this.developmentCards.get(DevCardType.SOLDIER).size();
		deck.yearOfPlenty = this.developmentCards.get(DevCardType.YEAR_OF_PLENTY).size();

		return deck;
	}

	private Collection<DevelopmentCard> makeDevelopmentDeck(
			DevCardType type, int j) {
		Collection<DevelopmentCard> typeDeck = new ArrayList<DevelopmentCard>();
		for (int i = 0; i < j; i++)
		{
			typeDeck.add(new DevelopmentCard(type));
		}
		return typeDeck;
	}

	private Collection<ResourceCard> makeTypeDeck(ResourceType type, int count) {
		Collection<ResourceCard> typeDeck = new ArrayList<ResourceCard>();
		if (count < 0) {
			count = 25;
		}
		for (int i = 0; i < count; i++) {
			typeDeck.add(new ResourceCard(type));
		}
		return typeDeck;
	}

	/**
	 * Returns a development card from the deck
	 *
	 * @param player
	 */
	public DevelopmentCard drawDevelopmentCard(long rngSeed) {
		RandomNumberGenerator rng = RandomNumberGenerator.getInstance(rngSeed);
		int rand = rng.generate(0, this.getDevelopmentCardCount(null));
		int soldierCount = this.getDevelopmentCardCount(DevCardType.SOLDIER);
		int monoplyCount = this.getDevelopmentCardCount(DevCardType.MONOPOLY);
		int monumentCount = this.getDevelopmentCardCount(DevCardType.MONUMENT);
		int roadBuildCount = this.getDevelopmentCardCount(DevCardType.ROAD_BUILD);
		int yearofPlentyCount = this.getDevelopmentCardCount(DevCardType.YEAR_OF_PLENTY);
		if (rand < soldierCount) {
			return this.getDevelopmentCard(DevCardType.SOLDIER);
		}
		rand -= (soldierCount - 1);
		if (rand < monoplyCount) {
			return this.getDevelopmentCard(DevCardType.MONOPOLY);
		}
		rand -= (monoplyCount - 1);
		if (rand < roadBuildCount) {
			return this.getDevelopmentCard(DevCardType.ROAD_BUILD);
		}
		rand -= (roadBuildCount - 1);
		if (rand < yearofPlentyCount) {
			return this.getDevelopmentCard(DevCardType.YEAR_OF_PLENTY);
		}
		rand -= (yearofPlentyCount - 1);
		if (rand < monumentCount) {
			return this.getDevelopmentCard(DevCardType.MONOPOLY);
		}
		return null;
	}

	private DevelopmentCard getDevelopmentCard(DevCardType type) {
		Iterator<DevelopmentCard> iter = this.developmentCards.get(type).iterator();
		DevelopmentCard card = iter.next();
		iter.remove();
		return card;
	}

	/**
	 * Returns a resource card of the desired type from the corresponding deck
	 *
	 * @param player
	 * @param type
	 */
	@Override
	public Collection<ResourceCard> removeResourceCard(ResourceType type,
			int count) {
		Collection<ResourceCard> removed = new ArrayList<ResourceCard>();

		Collection<ResourceCard> cards = this.resourceCards.get(type);

		if (!cards.isEmpty()) {
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
		if (!this.resourceCards.get(type).addAll(newCards))
		{
			added = false;
		}
		return added;
	}

	@Override
	public boolean addDevelopmentCardCollection(DevCardType type,
			Collection<DevelopmentCard> newCards) {
		boolean added = false;
		if (type != DevCardType.SOLDIER || type != DevCardType.MONUMENT)
		{
			added = this.playedCards.addAll(newCards);

		}
		return added;
	}

	@Override
	public int getDevelopmentCardCount(DevCardType type) {
		if (type == null) {
			int total = 0;
			for (Collection<DevelopmentCard> cards : this.developmentCards.values()) {
				total += cards.size();
			}
			return total;
		}

		return this.developmentCards.get(type).size();
	}

	/**
	 * Returns the number of available resource cards of the desired type in the
	 * bank
	 *
	 * @param type
	 *            the desired resource type
	 * @return the number of available resource cards of the desired type
	 */
	public int getResourceCardCount(ResourceType type) {
		if (type == ResourceType.ALL) {
			int totalCards = 0;
			for (ResourceType resourceType : ResourceType.values()) {
				if (resourceType != ResourceType.ALL && resourceType != ResourceType.NONE) {
					totalCards += this.resourceCards.get(resourceType).size();
				}
			}
			return totalCards;
		}
		else {
			return this.resourceCards.get(type).size();
		}
	}

	public Collection<DevelopmentCard> getPlayedCards() {
		return this.playedCards;
	}

	public void setPlayedCards(Collection<DevelopmentCard> playedCards) {
		this.playedCards = playedCards;
	}

}
