package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import clientBackend.transport.TransportBank;
import clientBackend.transport.TransportDeck;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

public class BankTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	Bank myBank;
	Bank transBank;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBank() {
		myBank = new Bank();
		for(ResourceType type: ResourceType.values()){
			assertTrue(myBank.getResourceCardCount(type)==25);
		}
		for(DevCardType type: DevCardType.values()){
			switch(type){
			case SOLDIER:
				assertTrue(myBank.getDevelopmentCardCount(type) == 14);
				break;
			case MONUMENT:
				assertTrue(myBank.getDevelopmentCardCount(type) == 5);
				break;
			default:
				assertTrue(myBank.getDevelopmentCardCount(type) == 2);
				break;
			}
			
		}
	}

	@Test

	public void testBankTransportDeckTransportBank() {
		//TransportDeck devDeck, TransportBank resDeck
		TransportDeck devDeck = new TransportDeck();
		TransportBank resDeck = new TransportBank();
		devDeck.monopoly = 1;
		devDeck.monument = 3;
		devDeck.roadBuilding = 2;
		devDeck.soldier = 10;
		devDeck.yearOfPlenty = 2;
		resDeck.brick = 12;
		resDeck.ore = 15;
		resDeck.sheep = 8;
		resDeck.wheat = 20;
		resDeck.wood = 5;
		transBank = new Bank(devDeck, resDeck);
		for(ResourceType type: ResourceType.values()){
			switch(type){
			case BRICK:
				assertTrue(transBank.getResourceCardCount(type)==12);
				break;
			case WOOD:
				assertTrue(transBank.getResourceCardCount(type)==5);
				break;
			case WHEAT:
				assertTrue(transBank.getResourceCardCount(type)==20);
				break;
			case ORE:
				assertTrue(transBank.getResourceCardCount(type)==15);
				break;
			case SHEEP:
				assertTrue(transBank.getResourceCardCount(type)==8);
				break;
			default:
				
				break;
			}
			
		}
		for(DevCardType type: DevCardType.values()){
			switch(type){
			case SOLDIER:
				assertTrue(transBank.getDevelopmentCardCount(type) == 10);
				break;
			case MONUMENT:
				assertTrue(transBank.getDevelopmentCardCount(type) == 3);
				break;
			case MONOPOLY:
				assertTrue(transBank.getDevelopmentCardCount(type) == 1);
				break;
			default:
				assertTrue(transBank.getDevelopmentCardCount(type) == 2);
				break;
			}
			
		}
		
	}

	@Test
	@Ignore
	public void testDrawDevelopmentCard() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddDevelopmentCardCollection() {
		for(DevCardType type: DevCardType.values())
		{
			myBank = new Bank();
			int initSize = myBank.getPlayedCards().size();
			Collection<DevelopmentCard> newCards = new ArrayList<DevelopmentCard>();
			newCards.add(new DevelopmentCard(type));
			if(myBank.addDevelopmentCardCollection(type, newCards)){
				assertTrue("The card had the right type and was added", initSize == myBank.getPlayedCards().size()-1);
			}
			else
			{
				assertTrue("The card was not added", initSize == myBank.getPlayedCards().size());
			}
		}
		
	}

}
