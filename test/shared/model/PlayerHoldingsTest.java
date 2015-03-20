package shared.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.model.DevelopmentCard;
import shared.model.Harbor;
import shared.model.PlayerHoldings;
import shared.model.ResourceCard;
import shared.transport.TransportNewDevCards;
import shared.transport.TransportOldDevCards;
import shared.transport.TransportPlayer;
import shared.transport.TransportResources;

/**
 * @author alexthomas
 *
 */
public class PlayerHoldingsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	private PlayerHoldings holdings1 = new PlayerHoldings();
	private PlayerHoldings holdings2 = new PlayerHoldings();
	private Map<ResourceType, Collection<ResourceCard>> resourceCards;
	HashMap<DevCardType, Collection<DevelopmentCard>> developmentCards;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		this.developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		for (ResourceType type : ResourceType.values())
		{
			Collection<ResourceCard> newPile = new ArrayList<ResourceCard>();
			for (int i = 0; i < 5; ++i)
			{
				newPile.add(new ResourceCard(type));
			}
			this.resourceCards.put(type, newPile);
		}
		for (DevCardType type : DevCardType.values())
		{
			Collection<DevelopmentCard> newPile = new ArrayList<DevelopmentCard>();
			for (int i = 0; i < 2; ++i)
			{
				if (i == 0)
				{
					newPile.add(new DevelopmentCard(type, false));
				}
				else
				{
					newPile.add(new DevelopmentCard(type, true));
				}
			}
			this.developmentCards.put(type, newPile);
		}
		this.holdings1.setResourceCards(this.resourceCards);
		this.holdings1.setDevelopmentCards(this.developmentCards);
		this.holdings2.setResourceCards(this.resourceCards);
		this.holdings2.setDevelopmentCards(this.developmentCards);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		for (ResourceType type : ResourceType.values())
		{
			this.resourceCards.get(type).clear();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test method for
	 * {@link shared.model.PlayerHoldings#removeResourceCard(shared.definitions.ResourceType, int)}
	 * .
	 */
	@Test
	public void testRemoveResourceCard() {
		ResourceType type = ResourceType.BRICK;
		int initCount = this.holdings1.getResourceCardCount(type);
		assertTrue("The count started corrctly!", initCount == 5);
		Collection<ResourceCard> removed;
		removed = this.holdings1.removeResourceCard(type, 3);
		int afterCount = this.holdings1.getResourceCardCount(type);
		assertTrue("The resources were removed.", afterCount == 2);
		assertTrue("There were the correct number in the removed", removed.size() == 3);
	}

	/**
	 * Test method for
	 * {@link shared.model.PlayerHoldings#addResourceCardCollection(shared.definitions.ResourceType, java.util.Collection)}
	 * .
	 */
	@Test
	public void testAddResourceCardCollection() {
		Collection<ResourceCard> adding = new ArrayList<ResourceCard>();
		ResourceType type = ResourceType.WOOD;
		for (int i = 0; i < 3; i++)
		{
			adding.add(new ResourceCard(type));
		}
		assertTrue("There are three wood", adding.size() == 3);
		assertTrue("The cards were added", this.holdings1.addResourceCardCollection(type, adding));
		int newSize = this.holdings1.getResourceCardCount(type);
		assertTrue("There are the correct number of cards", newSize == 8);
	}

	/**
	 * Test method for
	 * {@link shared.model.PlayerHoldings#removeDevelopmentCard(shared.definitions.DevCardType, int)}
	 * .
	 */
	@Test
	public void testRemoveDevelopmentCard() {
		int initCount;
		for (DevCardType type : DevCardType.values())
		{
			initCount = this.holdings2.getDevelopmentCardCount(type);
			assertTrue("correct ammount in start", initCount == 2);
			Collection<DevelopmentCard> removed;
			removed = this.holdings2.removeDevelopmentCard(type, 1);
			int removedSize = removed.size();
			int remainingSize = this.holdings2.getDevelopmentCardCount(type);
			assertTrue("correct amount in the removed", removedSize == 1);
			assertTrue("correct amount left in collection", remainingSize == 1);
			//removeing from an empty list
			this.holdings2.getDevelopmentCards().get(type).clear();
			removed = this.holdings2.removeDevelopmentCard(type, 1);
			assertTrue("The empty list removes nothing", removed.isEmpty());

		}

	}

	/**
	 * Test method for
	 * {@link shared.model.PlayerHoldings#addDevelopmentCardCollection(shared.definitions.DevCardType, java.util.Collection)}
	 * .
	 */
	@Test
	public void testAddDevelopmentCardCollection() {

		Collection<DevelopmentCard> adding = new ArrayList<DevelopmentCard>();
		for (DevCardType type : DevCardType.values())
		{
			adding.add(new DevelopmentCard(type));
			boolean added = this.holdings2.addDevelopmentCardCollection(type, adding);
			int addingSize = adding.size();

			if (added)
			{
				int afterSize = 0;
				switch (type) {
				case SOLDIER:
					afterSize = this.holdings2.getPlayedKnights().size();
					break;
				case MONUMENT:
					afterSize = this.holdings2.getPlayedMonuments().size();
					break;
				default:
					fail("This should not be reached");
				}
				assertTrue("correct amount added", afterSize == addingSize);
			}
			else {

				assertFalse("correct ammount was added", added);
			}
			adding.clear();
		}
	}

	@Test
	public void testPlayerHoldings() {
		PlayerHoldings myholding = new PlayerHoldings();
		for (DevCardType type : DevCardType.values()) {
			assertTrue(myholding.getDevelopmentCards().get(type).isEmpty());
		}
		for (ResourceType type : ResourceType.values()) {
			assertTrue(myholding.getResourceCards().get(type).isEmpty());
		}
		assertTrue(myholding.getHarbors().isEmpty());
		assertTrue(myholding.getPlayedKnights().isEmpty());
		assertTrue(myholding.getPlayedMonuments().isEmpty());
	}

	@Test
	public void testPlayerHoldingsTransportPlayerCollectionOfHarbor() {

		TransportPlayer play = new TransportPlayer();
		play.monuments = 3;
		TransportNewDevCards newDevCards = new TransportNewDevCards();
		newDevCards.monopoly = 0;
		newDevCards.monument = 2;
		newDevCards.roadBuilding = 1;
		newDevCards.soldier = 3;
		newDevCards.yearOfPlenty = 4;
		play.newDevCards = newDevCards;
		TransportOldDevCards old = new TransportOldDevCards();
		old.monopoly = 2;
		old.monument = 1;
		old.roadBuilding = 0;
		old.soldier = 1;
		old.yearOfPlenty = 5;
		play.oldDevCards = old;
		play.playerIndex = PlayerNumber.ONE;
		TransportResources res = new TransportResources();
		res.brick = 1;
		res.ore = 2;
		res.sheep = 3;
		res.wheat = 4;
		res.wood = 5;
		play.resources = res;
		play.soldiers = 5;
		play.victoryPoints = 3;

		Collection<Harbor> harbors = new ArrayList<Harbor>();
		Harbor h = new Harbor(null, null, null, 0);
		harbors.add(h);

		PlayerHoldings hold = new PlayerHoldings(play, harbors);
		assertTrue("number of horbors right", hold.getHarbors().size() == 1);
		assertTrue("number of soldiers", hold.getPlayedKnights().size() == 5);
		assertTrue("number of monuments", hold.getPlayedMonuments().size() == 3);
		for (ResourceType type : ResourceType.values()) {
			switch (type) {
			case BRICK:
				assertTrue("The correct number of brick", hold.getResourceCardCount(type) == 1);
				break;
			case WOOD:
				assertTrue("The correct number of brick", hold.getResourceCardCount(type) == 5);
				break;
			case WHEAT:
				assertTrue("The correct number of brick", hold.getResourceCardCount(type) == 4);
				break;
			case ORE:
				assertTrue("The correct number of ore", hold.getResourceCardCount(type) == 2);
				break;
			case SHEEP:
				assertTrue("The correct number of brick", hold.getResourceCardCount(type) == 3);
				break;
			default:
				break;
			}
		}
		for (DevCardType type : DevCardType.values()) {
			switch (type) {
			case MONOPOLY:
				assertTrue("The correct number of monopoly",
						hold.getDevelopmentCardCount(type) == 2);
				break;
			case MONUMENT:
				assertTrue("The correct number of monument",
						hold.getDevelopmentCardCount(type) == 3);
				break;
			case ROAD_BUILD:
				assertTrue("The correct number of road_building",
						hold.getDevelopmentCardCount(type) == 1);
				break;
			case SOLDIER:
				assertTrue("The correct number of soldier", hold.getDevelopmentCardCount(type) == 4);
				break;
			case YEAR_OF_PLENTY:
				assertTrue("The correct number of YOP", hold.getDevelopmentCardCount(type) == 9);
				break;
			default:
				break;
			}
		}

	}

}
