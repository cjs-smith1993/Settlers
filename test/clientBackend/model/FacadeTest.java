package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.definitions.PlayerNumber;
import shared.definitions.Status;
import clientBackend.transport.*;

public class FacadeTest {
	private Facade facade;
	TransportModel model;
	TransportPlayer player;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.facade = new Facade();
		this.model = new TransportModel();
		this.player = new TransportPlayer();

		this.model.deck = new TransportDeck();
		this.model.bank = new TransportBank();
		this.model.players = new TransportPlayer[1];
		this.model.map = new TransportMap();
		this.model.turnTracker = new TransportTurnTracker();
		this.model.chat = new TransportChat();
		this.model.chat.lines = new ArrayList<TransportLine>();
		this.model.log = new TransportLog();
		this.model.log.lines = new ArrayList<TransportLine>();

		this.model.turnTracker.status = Status.PLAYING;
		this.model.turnTracker.currentTurn = PlayerNumber.ONE;

		this.model.deck.yearOfPlenty = 2;
		this.model.deck.monopoly = 2;
		this.model.deck.soldier = 14;
		this.model.deck.roadBuilding = 2;
		this.model.deck.monument = 5;

		this.model.bank.brick = 23;
		this.model.bank.ore = 21;
		this.model.bank.sheep = 23;
		this.model.bank.wheat = 21;
		this.model.bank.wood = 23;

		this.player.resources = new TransportResources();
		this.player.oldDevCards = new TransportOldDevCards();
		this.player.newDevCards = new TransportNewDevCards();

		this.player.resources.brick = 2;
		this.player.resources.ore = 4;
		this.player.resources.sheep = 2;
		this.player.resources.wheat = 4;
		this.player.resources.wood = 2;

		this.player.oldDevCards.yearOfPlenty = 1;
		this.player.oldDevCards.monopoly = 1;
		this.player.oldDevCards.soldier = 1;
		this.player.oldDevCards.roadBuilding = 1;
		this.player.oldDevCards.monument = 1;

		this.player.roads = 15;
		this.player.settlements = 5;
		this.player.cities = 4;
		this.player.playerIndex = PlayerNumber.ONE;

		this.model.players[0] = this.player;

		this.model.map.hexes = new ArrayList<TransportHex>();
		this.model.map.roads = new ArrayList<TransportRoad>();
		this.model.map.cities = new ArrayList<TransportCity>();
		this.model.map.settlements = new ArrayList<TransportSettlement>();
		this.model.map.ports = new ArrayList<TransportPort>();
		this.model.map.robber = new TransportRobber();

		try {
			this.facade.initializeModel(this.model);
		} catch (CatanException e) {
			e.printStackTrace();
			fail();
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCanDiscardCards() throws CatanException {

		this.player.resources.brick = 8;
		this.player.resources.ore = 0;
		this.player.resources.sheep = 0;
		this.player.resources.wheat = 0;
		this.player.resources.wood = 0;
		this.model.players = new TransportPlayer[1];
		this.model.players[0] = this.player;
		this.model.turnTracker.status = Status.DISCARDING;
		this.facade.initializeModel(this.model);
		/*
		 * Should work because the status is discarding and the player has more
		 * than 7 cards
		 */
		assertTrue(this.facade.canDiscardCards(PlayerNumber.ONE));

		this.player.resources.brick = 7;
		this.model.players = new TransportPlayer[1];
		this.model.players[0] = this.player;
		this.facade.initializeModel(this.model);
		/* Should fail because the client player only has 7 resource cards */
		assertFalse(this.facade.canDiscardCards(PlayerNumber.ONE));

		this.model.turnTracker.status = Status.ROLLING;
		this.player.resources.brick = 8;
		this.model.players = new TransportPlayer[1];
		this.model.players[0] = this.player;
		this.facade.initializeModel(this.model);
		/* Should fail because the status is not discarding */
		assertFalse(this.facade.canDiscardCards(PlayerNumber.ONE));
	}

	@Test
	public void testCanRollNumber() throws CatanException {
		this.model.turnTracker.status = Status.ROLLING;
		this.facade.initializeModel(this.model);
		/*
		 * Should pass because it is the player's turn and the status is rolling
		 */
		assertTrue(this.facade.canRollNumber(PlayerNumber.ONE));

		this.model.turnTracker.currentTurn = PlayerNumber.TWO;
		this.facade.initializeModel(this.model);
		/* Should fail because it is not the player's turn */
		assertFalse(this.facade.canRollNumber(PlayerNumber.ONE));
	}

	@Test
	public void testCanBuildRoad() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanBuildSettlement() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanBuildCity() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanOfferTrade() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanAcceptTrade() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanMaritimeTrade() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanFinishTurn() throws CatanException {
		this.model.turnTracker.status = Status.PLAYING;
		this.facade.initializeModel(this.model);
		/*
		 * Should pass because it is the player's turn and the status is playing
		 */
		assertTrue(this.facade.canFinishTurn(PlayerNumber.ONE));
	}

	@Test
	public void testFinishTurn() throws CatanException {

	}

	@Test
	public void testCanBuyDevCard() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseYearOfPlenty() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseRoadBuilder() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseSoldier() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseMonopoly() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseMonument() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanPlaceRobber() {
		//fail("Not yet implemented");
	}

}
