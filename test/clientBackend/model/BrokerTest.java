package clientBackend.model;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import clientBackend.transport.TransportBank;
import clientBackend.transport.TransportDeck;
import clientBackend.transport.TransportPlayer;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class BrokerTest {

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
	@Ignore
	public void testProcessInvoice() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCanPurchase() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPurchase() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCanPlayDevelopmentCard() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPlayDevelopmentCard() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCanDiscardCards() {
		fail("Not yet implemented");
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

}
