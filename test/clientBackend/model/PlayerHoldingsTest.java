package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import shared.definitions.ResourceType;

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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		for(ResourceType type: ResourceType.values())
		{
			Collection<ResourceCard> newPile = new ArrayList<ResourceCard>();
			for(int i = 0; i < 5; ++i)
			{
				newPile.add(new ResourceCard(type));
			}
			resourceCards.put(type,newPile);
		}
		holdings1.setResourceCards(resourceCards);
		holdings2.setResourceCards(resourceCards);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		for(ResourceType type: ResourceType.values())
		{
			resourceCards.get(type).clear();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test method for {@link clientBackend.model.PlayerHoldings#removeResourceCard(shared.definitions.ResourceType, int)}.
	 */
	@Test
	public void testRemoveResourceCard() {
		ResourceType type = ResourceType.BRICK;
		int initCount = holdings1.getResourceCardCount(type);
		assertTrue("The count started corrctly!", initCount == 5);
		Collection<ResourceCard> removed;
		removed = holdings1.removeResourceCard(type, 3);
		int afterCount = holdings1.getResourceCardCount(type);
		assertTrue("The resources were removed.", afterCount == 2);
		assertTrue("There were the correct number in the removed", removed.size()==3);
	}

	/**
	 * Test method for {@link clientBackend.model.PlayerHoldings#addResourceCardCollection(shared.definitions.ResourceType, java.util.Collection)}.
	 */
	@Test
	public void testAddResourceCardCollection() {
		Collection<ResourceCard> adding = new ArrayList<ResourceCard>();
		ResourceType type = ResourceType.WOOD;
		for(int i = 0; i < 3; i++)
		{
			adding.add(new ResourceCard(type));
		}
		assertTrue("There are three wood", adding.size() == 3);
		assertTrue("The cards were added",holdings1.addResourceCardCollection(type, adding));
		int newSize = holdings1.getResourceCardCount(type);
		assertTrue("There are the correct number of cards", newSize == 8);
	}

	/**
	 * Test method for {@link clientBackend.model.PlayerHoldings#removeDevelopmentCard(shared.definitions.DevCardType, int)}.
	 */
	@Test
	@Ignore
	public void testRemoveDevelopmentCard() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link clientBackend.model.PlayerHoldings#addDevelopmentCardCollection(shared.definitions.DevCardType, java.util.Collection)}.
	 */
	@Test
	@Ignore
	public void testAddDevelopmentCardCollection() {
		fail("Not yet implemented");
	}

}
