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
		for(ResourceType type: ResourceType.values()){
			ResourceInvoice invoice = new ResourceInvoice(type, 3, PlayerNumber.BANK, PlayerNumber.ONE);
			try {
				assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
			} catch (CatanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(ResourceType type: ResourceType.values()){
			if(type != ResourceType.ALL && type != ResourceType.NONE)
			{
				assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.ONE).getResourceCardCount(type)==3);
				assertTrue("the number was correctly transfered", myBroker.getHoldings().get(PlayerNumber.BANK).getResourceCardCount(type)==22);
			}
		}
	}

	@Test
	public void testCanPurchase() throws CatanException {
		for(ResourceType type: ResourceType.values()){
			ResourceInvoice invoice = new ResourceInvoice(type, 3, PlayerNumber.BANK, PlayerNumber.ONE);
			try {
				assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
			} catch (CatanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	public void testCanMaritimeTrade(){
		for(ResourceType type: ResourceType.values()){
			ResourceInvoice invoice = new ResourceInvoice(type, 5, PlayerNumber.BANK, PlayerNumber.ONE);
			try {
				assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
			} catch (CatanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue("Can trade the cards",myBroker.CanMaritimeTrade(PlayerNumber.ONE));
		assertFalse("can not trade cards",myBroker.CanMaritimeTrade(PlayerNumber.TWO));
	}
	@Test
	public void testCanOfferTrade(){
		for(ResourceType type: ResourceType.values()){
			ResourceInvoice invoice = new ResourceInvoice(type, 3, PlayerNumber.BANK, PlayerNumber.ONE);
			try {
				assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
			} catch (CatanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue("Can offer the cards",myBroker.canOfferTrade(PlayerNumber.ONE));
		assertFalse("can not offer cards",myBroker.canOfferTrade(PlayerNumber.TWO));
		
	}
	@Test
	public void testCanDiscardCards() {
		for(ResourceType type: ResourceType.values()){
			ResourceInvoice invoice = new ResourceInvoice(type, 3, PlayerNumber.BANK, PlayerNumber.ONE);
			try {
				assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
			} catch (CatanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
