package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.definitions.Status;
import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;
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
		this.player.playedDevCard = false;

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
	public void testCanMaritimeTrade() throws CatanException{
		PlayerNumber one = PlayerNumber.ONE;
		TransportPort port = new TransportPort();
		port.direction = EdgeDirection.NorthEast;
		port.location = new TransportHexLocation();
		port.location.x=3;
		port.location.y = 3;
		port.ratio = 3;
		port.resource = ResourceType.ALL;
		model.map.ports.add(port);
		TransportSettlement TS = new TransportSettlement();
		TS.location = new TransportVertexLocation();
		TS.location.x = 3;
		TS.location.y = 3;
		TS.location.direction = VertexDirection.East;
		TS.owner = PlayerNumber.ONE;
		model.map.settlements.add(TS);
		this.facade.initializeModel(model);
		ResourceType giving = ResourceType.SHEEP;
		//Cannot trade
		assertFalse(this.facade.canMaritimeTrade(one, giving));
		assertTrue(this.facade.canMaritimeTrade(one, ResourceType.ORE));
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
	public void testCanBuyDevCard() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanPlayYearOfPlenty() throws CatanException {
		// 1. Test doesn't have a card
		this.model.players[0].oldDevCards.yearOfPlenty = 0;
		this.model.players[0].newDevCards.yearOfPlenty = 1;
		this.facade.initializeModel(model);
		/*
		 * Should not pass
		 */
		assertFalse(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK, ResourceType.WOOD));
		
		// 2. Test has a card
		this.model.players[0].oldDevCards.yearOfPlenty = 1;
		this.model.players[0].newDevCards.yearOfPlenty = 0;
		this.facade.initializeModel(model);
		/*
		 * Should pass
		 */
		assertTrue(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK, ResourceType.WOOD));
		
		// 3. Test has a card but player has already played another dev card
		this.model.players[0].oldDevCards.yearOfPlenty = 1;
		this.model.players[0].newDevCards.yearOfPlenty = 0;
		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(model);
		/*
		 * Should not pass
		 */
		assertFalse(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK, ResourceType.WOOD));
		
		// 4. Test bank doesn't have resources
		this.model.bank.wood = 0;
		this.model.players[0].playedDevCard = false;
		this.facade.initializeModel(model);
		/*
		 * Should not pass
		 */
		assertFalse(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK, ResourceType.WOOD));
		
	}

	@Test
	public void testCanUseRoadBuilder() throws CatanException {
		this.model.turnTracker.status = Status.ROLLING;
		this.model.players[0].oldDevCards.roadBuilding = 1;
		this.facade.initializeModel(this.model);

		// 1. Not in playing state
		assertFalse("cannot play card when not in the playing state",
				this.facade.canUseRoadBuilder(PlayerNumber.ONE));

		this.model.turnTracker.status = Status.PLAYING;
		this.facade.initializeModel(this.model);

		// 2. Not your turn
		assertFalse("cannot play card when it is not your turn",
				this.facade.canUseRoadBuilder(PlayerNumber.TWO));

		// 3. Cannot play when you just bought the card
		this.model.players[0].oldDevCards.roadBuilding = 0;
		this.model.players[0].newDevCards.roadBuilding = 1;
		this.facade.initializeModel(this.model);
		assertFalse("cannot play a card on the turn the card was bought",
				this.facade.canUseRoadBuilder(PlayerNumber.ONE));

		this.model.players[0].oldDevCards.roadBuilding = 2;
		this.model.players[0].newDevCards.roadBuilding = 0;
		this.facade.initializeModel(this.model);

		// 4. Can play when it is your turn
		assertTrue("can play road building when it is your turn",
				this.facade.canUseRoadBuilder(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);

		// 5. Cannot play when you've already played a card
		assertFalse("cannot play more than one card per turn",
				this.facade.canUseRoadBuilder(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = false;
		this.model.players[0].roads = 1;
		this.facade.initializeModel(this.model);

		// 6. Cannot play when you don't have enough roads
		assertFalse("cannot play with only one available road",
				this.facade.canUseRoadBuilder(PlayerNumber.ONE));
	}

	@Test
	public void testCanUseSoldier() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseMonopoly() throws CatanException {
		this.model.turnTracker.status = Status.ROLLING;
		this.model.players[0].oldDevCards.roadBuilding = 1;
		this.facade.initializeModel(this.model);

		// 1. Not in playing state
		assertFalse("cannot play card when not in the playing state",
				this.facade.canUseMonopoly(PlayerNumber.ONE));

		this.model.turnTracker.status = Status.PLAYING;
		this.facade.initializeModel(this.model);

		// 2. Not your turn
		assertFalse("cannot play card when it is not your turn",
				this.facade.canUseMonopoly(PlayerNumber.TWO));

		// 3. Cannot play when you just bought the card
		this.model.players[0].oldDevCards.monopoly = 0;
		this.model.players[0].newDevCards.monopoly = 1;
		this.facade.initializeModel(this.model);
		assertFalse("cannot play a card on the turn the card was bought",
				this.facade.canUseMonopoly(PlayerNumber.ONE));

		this.model.players[0].oldDevCards.monopoly = 2;
		this.model.players[0].newDevCards.monopoly = 0;
		this.facade.initializeModel(this.model);

		// 4. Can play when it is your turn
		assertTrue("can play monopoly when it is your turn",
				this.facade.canUseMonopoly(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);

		// 5. Cannot play when you've already played a card
		assertFalse("cannot play more than one card per turn",
				this.facade.canUseMonopoly(PlayerNumber.ONE));
	}

	@Test
	public void testCanUseMonument() throws CatanException {
		this.model.turnTracker.status = Status.ROLLING;
		this.model.players[0].oldDevCards.monument = 1;
		this.facade.initializeModel(this.model);

		// 1. Not in playing state
		assertFalse("cannot play card when not in the playing state",
				this.facade.canUseMonument(PlayerNumber.ONE));

		this.model.turnTracker.status = Status.PLAYING;
		this.facade.initializeModel(this.model);

		// 2. Not your turn
		assertFalse("cannot play card when it is not your turn",
				this.facade.canUseMonument(PlayerNumber.TWO));

		// 3. Cannot play when you just bought the card
		this.model.players[0].oldDevCards.monument = 0;
		this.model.players[0].newDevCards.monument = 1;
		this.facade.initializeModel(this.model);
		assertFalse("cannot play a card on the turn the card was bought",
				this.facade.canUseMonument(PlayerNumber.ONE));

		this.model.players[0].oldDevCards.monument = 2;
		this.model.players[0].newDevCards.monument = 0;
		this.facade.initializeModel(this.model);

		// 4. Can play when it is your turn
		assertTrue("can play monopoly when it is your turn",
				this.facade.canUseMonument(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);

		// 5. Can play when you've already played another dev card
		assertTrue("can play more than one monument per turn",
				this.facade.canUseMonument(PlayerNumber.ONE));
	}

	@Test
	public void testCanPlaceRobber() {
		//fail("Not yet implemented");
	}

}
