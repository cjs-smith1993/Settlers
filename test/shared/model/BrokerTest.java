package shared.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;
import shared.model.Broker;
import shared.model.CatanException;
import shared.model.PlayerHoldings;
import shared.model.ResourceInvoice;

public class BrokerTest {
	public Broker myBroker;
	public Broker transBroker;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		this.myBroker = new Broker();
	}

	@Test
	public void testBroker() {
		for (PlayerNumber player : PlayerNumber.values()) {
			switch (player) {
			case BANK:
				int dev = this.myBroker.getHoldings().get(player).getDevelopmentCardCount(null);
				assertTrue(dev == 25);
				int res = 0;
				for (ResourceType type : ResourceType.values()) {
					if (type != ResourceType.ALL && type != ResourceType.NONE) {
						res += this.myBroker.getHoldings().get(player).getResourceCardCount(type);
					}
				}
				assertTrue(res == 95);
				break;
			default:
				PlayerHoldings holding = (PlayerHoldings) this.myBroker.getHoldings().get(player);
				for (DevCardType type : DevCardType.values()) {
					assertTrue(holding.getDevelopmentCards().get(type).isEmpty());
				}
				for (ResourceType type : ResourceType.values()) {
					assertTrue(holding.getResourceCards().get(type).isEmpty());
				}
				assertTrue(holding.getHarbors().isEmpty());
				assertTrue(holding.getPlayedKnights().isEmpty());
				assertTrue(holding.getPlayedMonuments().isEmpty());

				break;
			}
		}
	}

	/*
	 * @Test
	 * 
	 * @Ignore public void
	 * testBrokerTransportBankTransportDeckCollectionOfTransportPlayerMapOfPlayerNumberCollectionOfHarbor
	 * () { TransportDeck devDeck = new TransportDeck(); TransportBank resDeck =
	 * new TransportBank(); devDeck.monopoly = 1; devDeck.monument = 3;
	 * devDeck.roadBuilding = 2; devDeck.soldier = 10; devDeck.yearOfPlenty = 2;
	 * resDeck.brick = 12; resDeck.ore = 15; resDeck.sheep = 8; resDeck.wheat =
	 * 20; resDeck.wood = 5; //transBroker = new Broker(resDeck,DevDeck);
	 * 
	 * /*TransportBank resources, TransportDeck bankDevCard,
	 * Collection<TransportPlayer> playerList, Map<PlayerNumber,
	 * Collection<Harbor>> harborMap
	 */
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
			e.printStackTrace();
		}
		
		for (ResourceType type : ResourceType.values()) {
			if (type != ResourceType.ALL && type != ResourceType.NONE) {
				assertTrue("the number was correctly transfered",
						this.myBroker.getHoldings().get(PlayerNumber.ONE)
								.getResourceCardCount(type) == 3);
				assertTrue("the number was correctly transfered",
						this.myBroker.getHoldings().get(PlayerNumber.BANK)
								.getResourceCardCount(type) == 16);
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
			e.printStackTrace();
		}
		for (ResourceType type : ResourceType.values()) {
			if (type != ResourceType.ALL && type != ResourceType.NONE) {
				assertTrue("the number was correctly transfered",
						this.myBroker.getHoldings().get(PlayerNumber.TWO)
								.getResourceCardCount(type) == 2);
				assertTrue(
						"the number was correctly transfered",
						this.myBroker.getHoldings().get(PlayerNumber.BANK)
								.getResourceCardCount(type) == 14);
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
			e.printStackTrace();
		}
		
		for (ResourceType type : ResourceType.values()) {
			if (type != ResourceType.ALL && type != ResourceType.NONE) {
				switch (type) {
				case BRICK:
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.TWO).getResourceCardCount(ResourceType.BRICK) == 4);
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.ONE).getResourceCardCount(ResourceType.BRICK) == 1);
					break;
				case WOOD:
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.TWO).getResourceCardCount(ResourceType.WOOD) == 4);
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.ONE).getResourceCardCount(ResourceType.WOOD) == 1);
					break;
				case WHEAT:
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.TWO).getResourceCardCount(ResourceType.WHEAT) == 1);
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.ONE).getResourceCardCount(ResourceType.WHEAT) == 4);
					break;
				case ORE:
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.TWO).getResourceCardCount(ResourceType.ORE) == 0);
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.ONE).getResourceCardCount(ResourceType.ORE) == 5);
					break;
				case SHEEP:
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.TWO).getResourceCardCount(ResourceType.SHEEP) == 4);
					assertTrue("the number was correctly transfered", this.myBroker.getHoldings()
							.get(PlayerNumber.ONE).getResourceCardCount(ResourceType.SHEEP) == 1);
					break;
				default:
					break;
				}
			}
		}
	}

	@Test
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
			e.printStackTrace();
		}
		for (PropertyType type : PropertyType.values()) {
			this.thrown.expect(CatanException.class);
			this.myBroker.canPurchase(PlayerNumber.BANK, type);
			try {
				assertTrue("Holding that has enough",
						this.myBroker.canPurchase(PlayerNumber.ONE, type));
				assertFalse("Holding that is lacking",
						this.myBroker.canPurchase(PlayerNumber.TWO, type));
			} catch (CatanException e) {
				e.printStackTrace();
			}

		}

	}

	/*
	 * @Test
	 * 
	 * @Ignore public void testPurchase() { fail("Not yet implemented"); }
	 */

	@Test
	public void testCanPlayDevelopmentCard() {
		try {
			assertFalse(this.myBroker
					.canPlayDevelopmentCard(PlayerNumber.ONE, DevCardType.MONOPOLY));
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Test
	 * 
	 * @Ignore public void testPlayDevelopmentCard() {
	 * fail("Not yet implemented"); }
	 */

	@Test
	public void testCanMaritimeTrade() {
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice.setBrick(5);
		invoice.setOre(5);
		invoice.setSheep(5);
		invoice.setWheat(5);
		invoice.setWood(5);
		try {
			assertTrue("The invoice was processed!", this.myBroker.processInvoice(invoice));
		} catch (CatanException e) {
			e.printStackTrace();
		}
		assertTrue("Can trade the cards",
				this.myBroker.canMaritimeTrade(PlayerNumber.ONE, ResourceType.BRICK));
		assertFalse("can not trade cards",
				this.myBroker.canMaritimeTrade(PlayerNumber.TWO, ResourceType.BRICK));
	}

	@Test
	public void testCanOfferTrade() {
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

		assertTrue("Can offer the cards", this.myBroker.canOfferTrade(invoice));
		assertTrue("can Accept the trade", this.myBroker.canAcceptTrade(invoice));
	}

}
