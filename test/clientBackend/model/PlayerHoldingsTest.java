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
import org.junit.Test;

import shared.definitions.DevCardType;
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
	HashMap<DevCardType, Collection<DevelopmentCard>> developmentCards;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		resourceCards = new HashMap<ResourceType, Collection<ResourceCard>>();
		developmentCards = new HashMap<DevCardType, Collection<DevelopmentCard>>();
		for(ResourceType type: ResourceType.values())
		{
			Collection<ResourceCard> newPile = new ArrayList<ResourceCard>();
			for(int i = 0; i < 5; ++i)
			{
				newPile.add(new ResourceCard(type));
			}
			resourceCards.put(type,newPile);
		}
		for(DevCardType type: DevCardType.values())
		{
			Collection<DevelopmentCard> newPile = new ArrayList<DevelopmentCard>();
			for(int i = 0; i < 2; ++i)
			{
				if(i == 0)
				{
					newPile.add(new DevelopmentCard(type, false));
				}
				else
				{
					newPile.add(new DevelopmentCard(type, true));
				}
			}
			developmentCards.put(type, newPile);
		}
		holdings1.setResourceCards(resourceCards);
		holdings1.setDevelopmentCards(developmentCards);
		holdings2.setResourceCards(resourceCards);
		holdings2.setDevelopmentCards(developmentCards);
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
	public void testRemoveDevelopmentCard() {
		int initCount;
		for(DevCardType type: DevCardType.values())
		{
			initCount = holdings2.getDevelopmentCardCount(type);			
			assertTrue("correct ammount in start", initCount == 2);
			Collection<DevelopmentCard> removed;
			removed = holdings2.removeDevelopmentCard(type, 1);
			int removedSize = removed.size();
			int remainingSize = holdings2.getDevelopmentCardCount(type);
			assertTrue("correct amount in the removed", removedSize == 1);
			assertTrue("correct amount left in collection", remainingSize == 1);
			//removeing from an empty list
			holdings2.getDevelopmentCards().get(type).clear();
			removed = holdings2.removeDevelopmentCard(type, 1);
			assertTrue("The empty list removes nothing", removed.isEmpty());
			
		}
		
		
	}

	/**
	 * Test method for {@link clientBackend.model.PlayerHoldings#addDevelopmentCardCollection(shared.definitions.DevCardType, java.util.Collection)}.
	 */
	@Test

	public void testAddDevelopmentCardCollection() {

		Collection<DevelopmentCard> adding = new ArrayList<DevelopmentCard>();
		for(DevCardType type: DevCardType.values())
		{
			adding.add(new DevelopmentCard(type));
			boolean added = holdings2.addDevelopmentCardCollection(type,adding);
			int addingSize = adding.size();

			if(added)
			{
				int afterSize = 0;
				switch(type){
					case SOLDIER:
						afterSize = holdings2.getPlayedKnights().size();
						break;
					case MONUMENT:
						afterSize = holdings2.getPlayedMonuments().size();
						break;
					default:
							fail("This should not be reached");
				}
				assertTrue("correct amount added", afterSize == addingSize);
			}else{
				
				assertFalse("correct ammount was added", added);
			}
			adding.clear();
		}
	}

}
