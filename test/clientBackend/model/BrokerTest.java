package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import clientBackend.transport.TransportBank;
import clientBackend.transport.TransportDeck;
import clientBackend.transport.TransportNewDevCards;
import clientBackend.transport.TransportOldDevCards;
import clientBackend.transport.TransportPlayer;
import clientBackend.transport.TransportResources;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;

public class BrokerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	public Broker myBroker;
	public Broker transBroker;
	@Before
	public void setUp() throws Exception {
		myBroker = new Broker();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBroker() {
		for(PlayerNumber player: PlayerNumber.values()){
			switch(player){
			case BANK:
				int dev = myBroker.getHoldings().get(player).getDevelopmentCardCount(null);
				assertTrue(dev == 25);
				int res = 0;
				for(ResourceType type: ResourceType.values()){
					res += myBroker.getHoldings().get(player).getResourceCardCount(type);
				}
				assertTrue(res == 175);
				break;
			default:
					PlayerHoldings holding = (PlayerHoldings) myBroker.getHoldings().get(player);
					for(DevCardType type: DevCardType.values()){
						assertTrue(holding.getDevelopmentCards().get(type).isEmpty());
					}
					for(ResourceType type: ResourceType.values()){
						assertTrue(holding.getResourceCards().get(type).isEmpty());
					}
					assertTrue(holding.getHarbors().isEmpty());
					assertTrue(holding.getPlayedKnights().isEmpty());
					assertTrue(holding.getPlayedMonuments().isEmpty());
					
				break;
			}
		}
	}

	/*@Test
	@Ignore
	public void testBrokerTransportBankTransportDeckCollectionOfTransportPlayerMapOfPlayerNumberCollectionOfHarbor() {
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
		//transBroker = new Broker(resDeck,DevDeck);
		
		/*TransportBank resources, 
		TransportDeck bankDevCard, 
		Collection<TransportPlayer> playerList,
		Map<PlayerNumber, 
		Collection<Harbor>> harborMap*/
	//}

	@Test
	public void testProcessInvoice() {
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice.setBrick(3);
		invoice.setOre(3);
		invoice.setSheep(3);
		invoice.setWheat(3);
		invoice.setWood(3);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ResourceType type: ResourceType.values()){
			if(type != ResourceType.ALL && type != ResourceType.NONE)
			{
				assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(type)==3);
				assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.BANK).getResourceCardCount(type)==22);
			}
		}
		invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.TWO);
		invoice.setBrick(2);
		invoice.setOre(2);
		invoice.setSheep(2);
		invoice.setWheat(2);
		invoice.setWood(2);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ResourceType type: ResourceType.values()){
			if(type != ResourceType.ALL && type != ResourceType.NONE)
			{
				assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.TWO).getResourceCardCount(type)==2);
				assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.BANK).getResourceCardCount(type)==20);
			}
		}
		invoice = new ResourceInvoice(PlayerNumber.ONE, PlayerNumber.TWO);
		invoice.setBrick(2);
		invoice.setOre(-2);
		invoice.setSheep(2);
		invoice.setWheat(-1);
		invoice.setWood(2);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ResourceType type: ResourceType.values()){
			if(type != ResourceType.ALL && type != ResourceType.NONE)
			{
				switch(type){
				case BRICK:
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.TWO).getResourceCardCount(ResourceType.BRICK)==4);
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(ResourceType.BRICK)==1);
					break;
				case WOOD:
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.TWO).getResourceCardCount(ResourceType.WOOD)==4);
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(ResourceType.WOOD)==1);
					break;
				case WHEAT:
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.TWO).getResourceCardCount(ResourceType.WHEAT)==1);
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(ResourceType.WHEAT)==4);
					break;
				case ORE:
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.TWO).getResourceCardCount(ResourceType.ORE)==0);
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(ResourceType.ORE)==5);
					break;
				case SHEEP:
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.TWO).getResourceCardCount(ResourceType.SHEEP)==4);
					assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(ResourceType.SHEEP)==1);
					break;
				}
				
			}
		}
	}

	@Test
	@Ignore
	public void testCanPurchase() throws CatanException {
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice.setBrick(3);
		invoice.setOre(3);
		invoice.setSheep(3);
		invoice.setWheat(3);
		invoice.setWood(3);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(PropertyType type: PropertyType.values()){
			this.thrown.expect(CatanException.class);
			this.myBroker.canPurchase(PlayerNumber.BANK, type);
			try {
				assertTrue("Holding that has enough", myBroker.canPurchase(PlayerNumber.ONE, type));
				assertFalse("Holding that is lacking", myBroker.canPurchase(PlayerNumber.TWO, type));
			} catch (CatanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/*@Test
	@Ignore
	public void testPurchase() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testCanPlayDevelopmentCard() {
		try {
			assertFalse(myBroker.canPlayDevelopmentCard(PlayerNumber.ONE, DevCardType.MONOPOLY));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*@Test
	@Ignore
	public void testPlayDevelopmentCard() {
		fail("Not yet implemented");
	}*/

	@Test
	@Ignore
	public void testCanMaritimeTrade(){
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice.setBrick(5);
		invoice.setOre(5);
		invoice.setSheep(5);
		invoice.setWheat(5);
		invoice.setWood(5);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue("Can trade the cards",myBroker.canMaritimeTrade(PlayerNumber.ONE));
		assertFalse("can not trade cards",myBroker.canMaritimeTrade(PlayerNumber.TWO));
	}
	@Test
	public void testCanOfferTrade(){
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice.setBrick(3);
		invoice.setOre(3);
		invoice.setSheep(3);
		invoice.setWheat(3);
		invoice.setWood(3);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			
			e.printStackTrace();
		}
		invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.TWO);
		invoice.setBrick(2);
		invoice.setOre(2);
		invoice.setSheep(2);
		invoice.setWheat(2);
		invoice.setWood(2);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			
			e.printStackTrace();
		}
		invoice = new ResourceInvoice(PlayerNumber.TWO, PlayerNumber.ONE);
		invoice.setBrick(-3);
		invoice.setOre(2);
		invoice.setSheep(0);
		invoice.setWheat(0);
		invoice.setWood(0);
		
		assertTrue("Can offer the cards",myBroker.canOfferTrade(invoice));
		assertTrue("can Accept the trade",myBroker.canAcceptTrade(invoice));
		
	}
	@Test
	@Ignore
	public void testCanDiscardCards() {
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice.setBrick(3);
		invoice.setOre(3);
		invoice.setSheep(3);
		invoice.setWheat(3);
		invoice.setWood(3);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue("Can remove the cards",myBroker.canDiscardCards(PlayerNumber.ONE, 7));
		assertFalse("can not remove cards",myBroker.canDiscardCards(PlayerNumber.TWO, 7));
		
	}

	@Test
	@Ignore
	public void testHasNecessaryResourceAmount() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHasResourceCard() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHasHarbor() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHasDevelopmentCard() {
		fail("Not yet implemented");
	}
/*
 * cnadiscard
 * canoffertrade
 * canmaitimtrade
 * buy dev card
 * use dev card
 */
}
